package com.aiinty.copayment.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.SignUpData
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.usecase.auth.RecoverUseCase
import com.aiinty.copayment.domain.usecase.auth.RefreshTokenUseCase
import com.aiinty.copayment.domain.usecase.auth.SignInUseCase
import com.aiinty.copayment.domain.usecase.auth.SignUpUseCase
import com.aiinty.copayment.domain.usecase.auth.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.auth.VerifyOTPUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val userPreferences: UserPreferences,
    private val signInUseCase: SignInUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase,
    private val recoverUseCase: RecoverUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
): BaseViewModel(errorHandler) {
    private val _resendCooldownSeconds = MutableStateFlow(0)
    val resendCooldownSeconds: StateFlow<Int> = _resendCooldownSeconds.asStateFlow()
    val canResend = MutableStateFlow(true)

    fun startResendCooldown(seconds: Int) {
        viewModelScope.launch {
            canResend.value = false
            _resendCooldownSeconds.value = seconds
            while (_resendCooldownSeconds.value > 0) {
                delay(1000)
                _resendCooldownSeconds.value -= 1
            }
            canResend.value = true
        }
    }

    fun startSplashLogic() {
        viewModelScope.launch {
            val refreshToken = userPreferences.getRefreshToken()
            val firstLaunch = userPreferences.getFirstLaunch()

            if (refreshToken != null)  {
                val result = refreshToken(refreshToken)
                if (result.isSuccess) {
                    val storedPin = userPreferences.getUserPin()
                    if (storedPin == null) {
                        emitNavigation(NavigationRoute.CreatePinCodeScreen(
                            nextDestination = NavigationRoute.PinCodeScreen.route
                        ))
                    } else {
                        emitNavigation(NavigationRoute.PinCodeScreen)
                    }
                    return@launch
                }
            }

            when {
                !firstLaunch -> {
                    emitNavigation(NavigationRoute.SignInScreen)
                }
                else -> {
                    emitNavigation(NavigationRoute.OnboardingScreen)
                }
            }
            if (firstLaunch) {
                userPreferences.saveFirstLaunch(false)
            }
        }
    }

    fun signIn(email:String, password:String) {
        //Saving in preferences because there is no endpoint for resending the email confirmation
        userPreferences.saveUserEmail(email)
        userPreferences.saveUserPassword(password)
        viewModelScope.launch {
            val result = signInUseCase.invoke(email, password)
            result.fold(
                onSuccess = {
                    val storedPin = userPreferences.getUserPin()
                    if (storedPin == null) {
                        emitNavigation(NavigationRoute.CreatePinCodeScreen(
                            nextDestination = NavigationRoute.PinCodeScreen.route
                        ))
                    } else {
                        emitNavigation(NavigationRoute.PinCodeScreen)
                    }
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    suspend fun refreshToken(refreshToken: String) : Result<Unit> {
        return withContext(Dispatchers.IO) {
            val result = refreshTokenUseCase.invoke(refreshToken)
            result.fold(
                onSuccess = {
                    Result.success(Unit)
                },
                onFailure = { e ->
                    handleError(e)
                    Result.failure(e)
                }
            )
        }
    }

    fun signUp(fullName: String, email: String, password: String) {
        //Saving in preferences because there is no endpoint for resending the email confirmation
        userPreferences.saveUserEmail(email)
        userPreferences.saveUserPassword(password)
        viewModelScope.launch {
            val data = SignUpData(full_name = fullName)
            val result = signUpUseCase.invoke(email = email, password = password, data = data)
            result.fold(
                onSuccess = {
                    emitNavigation(NavigationRoute.VerifyOTPScreen(
                        type = OTPType.EMAIL,
                        email = email,
                        nextDestination = NavigationRoute.CreatePinCodeScreen(
                            nextDestination = NavigationRoute.PinCodeScreen.route
                        ).route
                    ))
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    fun verifyOTP(type: OTPType, email: String, token: String) {
        viewModelScope.launch {
            val result = verifyOTPUseCase.invoke(type, email, token)
            result.fold(
                onSuccess = {
                    emitNavigation(NavigationRoute.NextScreen)
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    fun resendOTP(type: OTPType, email: String) {
        if (!canResend.value) return
        canResend.value = false
        viewModelScope.launch {
            val result = when(type) {
                OTPType.EMAIL -> {
                    val password = userPreferences.getUserPassword()
                    if (password != null) {
                        signUpUseCase.invoke(email = email, password = password, data = null)
                    } else {
                        Result.failure(Exception())
                    }
                }
                OTPType.EMAIL_CHANGE -> {
                    val newEmail = userPreferences.getUserNewEmail()
                    if (newEmail != null) {
                        updateUserUseCase.invoke(email = newEmail, password = null)
                    } else {
                        Result.failure(Exception())
                    }
                }
                OTPType.RECOVERY -> recoverUseCase.invoke(email)
            }
            result.fold(
                onSuccess = {
                    startResendCooldown(60)
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    fun recover(email: String) {
        viewModelScope.launch {
            val result = recoverUseCase.invoke(email)
            result.fold(
                onSuccess = {
                    emitNavigation(NavigationRoute.VerifyOTPScreen(
                        type = OTPType.RECOVERY,
                        email = email,
                        nextDestination = NavigationRoute.PasswordChangeScreen(
                            nextDestination = NavigationRoute.SignInScreen.route
                        ).route
                    ))
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    fun updateUser(email: String?, password: String?) {
        viewModelScope.launch {
            val result = updateUserUseCase.invoke(email, password)
            result.fold(
                onSuccess = {
                    emitNavigation(NavigationRoute.NextScreen)
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    fun createPin(pin: String) {
        viewModelScope.launch {
            try {
                if (pin.length == 5 && pin.all { it.isDigit() }) {
                    userPreferences.saveUserPin(pin)
                    emitNavigation(NavigationRoute.NextScreen)
                } else {
                   throw AppException.UiResError(R.string.error_invalid_pin_format)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun loginWithPin(pin: String) {
        viewModelScope.launch {
            try {
                val storedPin = userPreferences.getUserPin()
                if (storedPin == null) {
                    emitNavigation(NavigationRoute.CreatePinCodeScreen(
                        nextDestination = NavigationRoute.PinCodeScreen.route
                    ))
                } else if (pin == storedPin) {
                    emitNavigation(NavigationRoute.HomeScreen)
                } else {
                    throw AppException.UiResError(R.string.error_invalid_pin)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
}
