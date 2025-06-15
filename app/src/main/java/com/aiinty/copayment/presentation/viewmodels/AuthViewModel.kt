package com.aiinty.copayment.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.auth.SignUpData
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.usecase.auth.RecoverUseCase
import com.aiinty.copayment.domain.usecase.auth.RefreshTokenUseCase
import com.aiinty.copayment.domain.usecase.auth.SignInUseCase
import com.aiinty.copayment.domain.usecase.auth.SignUpUseCase
import com.aiinty.copayment.domain.usecase.auth.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.auth.VerifyOTPUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
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
    private val navigationEventBus: NavigationEventBus,
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

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    private suspend fun navigateToCreatePinInternal(next: String = NavigationRoute.PinCodeScreen.route) {
        navigateTo(NavigationRoute.CreatePinCodeScreen(nextDestination = next).route)
    }

    private suspend fun navigateToPinInternal() {
        navigateTo(NavigationRoute.PinCodeScreen.route)
    }

    private suspend fun navigateToHomeInternal() {
        navigateTo(NavigationRoute.HomeScreen.route)
    }

    private suspend fun navigateToSignInInternal() {
        navigateTo(NavigationRoute.SignInScreen.route)
    }

    private suspend fun navigateToOnboardingInternal() {
        navigateTo(NavigationRoute.OnboardingScreen.route)
    }

    fun navigateToForgotPassword() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.RecoverScreen.route)
        }
    }

    fun navigateToSignIn() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.SignInScreen.route)
        }
    }

    fun navigateToSignUp() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.SignUpScreen.route)
        }
    }

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
                        navigateToCreatePinInternal()
                    } else {
                        navigateToPinInternal()
                    }
                    return@launch
                }
            }
            if (!firstLaunch) {
                navigateToSignInInternal()
            } else {
                navigateToOnboardingInternal()
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
            val result = signInUseCase(email, password)
            result.fold(
                onSuccess = {
                    val storedPin = userPreferences.getUserPin()
                    if (storedPin == null) {
                        navigateToCreatePinInternal()
                    } else {
                        navigateToPinInternal()
                    }
                },
                onFailure = ::handleError
            )
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            refreshTokenUseCase(refreshToken).fold(
                onSuccess = { Result.success(Unit) },
                onFailure = {
                    handleError(it)
                    Result.failure(it)
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
            val result = signUpUseCase(email = email, password = password, data = data)
            result.fold(
                onSuccess = {
                    val nextRoute = NavigationRoute.CreatePinCodeScreen(
                        nextDestination = NavigationRoute.PinCodeScreen.route
                    ).route
                    val otpRoute = NavigationRoute.VerifyOTPScreen(
                        type = OTPType.EMAIL,
                        email = email,
                        nextDestination = nextRoute
                    ).route
                    navigateTo(otpRoute)
                },
                onFailure = ::handleError
            )
        }
    }

    fun verifyOTP(type: OTPType, email: String, token: String, next: String) {
        viewModelScope.launch {
            val result = verifyOTPUseCase(type, email, token)
            result.fold(
                onSuccess = { navigateTo(next) },
                onFailure = ::handleError
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
                        signUpUseCase(email = email, password = password, data = null)
                    } else {
                        Result.failure(Exception("Missing password"))
                    }
                }
                OTPType.EMAIL_CHANGE -> {
                    val newEmail = userPreferences.getUserNewEmail()
                    if (newEmail != null) {
                        updateUserUseCase(newEmail, null)
                    } else {
                        Result.failure(Exception("Missing new email"))
                    }
                }
                OTPType.RECOVERY -> recoverUseCase(email)
            }
            result.fold(
                onSuccess = { startResendCooldown(60) },
                onFailure = ::handleError
            )
        }
    }

    fun recover(email: String) {
        viewModelScope.launch {
            val result = recoverUseCase(email)
            result.fold(
                onSuccess = {
                    val nextRoute = NavigationRoute.PasswordChangeScreen(
                        nextDestination = NavigationRoute.SignInScreen.route
                    ).route
                    val otpRoute = NavigationRoute.VerifyOTPScreen(
                        type = OTPType.RECOVERY,
                        email = email,
                        nextDestination = nextRoute
                    ).route
                    navigateTo(otpRoute)
                },
                onFailure = ::handleError
            )
        }
    }

    fun updateUser(email: String?, password: String?, next: String) {
        viewModelScope.launch {
            val result = updateUserUseCase(email, password)
            result.fold(
                onSuccess = { navigateTo(next) },
                onFailure = ::handleError
            )
        }
    }

    fun createPin(pin: String, next: String) {
        viewModelScope.launch {
            try {
                if (pin.length == 5 && pin.all { it.isDigit() }) {
                    userPreferences.saveUserPin(pin)
                    navigateTo(next)
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
                    navigateToCreatePinInternal()
                } else if (pin == storedPin) {
                    navigateToHomeInternal()
                } else {
                    throw AppException.UiResError(R.string.error_invalid_pin)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }
}
