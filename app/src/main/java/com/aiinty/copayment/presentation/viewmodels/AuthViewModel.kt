package com.aiinty.copayment.presentation.viewmodels

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.SignUpData
import com.aiinty.copayment.data.network.ApiErrorCode
import com.aiinty.copayment.data.network.ApiException
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.usecase.RecoverUseCase
import com.aiinty.copayment.domain.usecase.RefreshTokenUseCase
import com.aiinty.copayment.domain.usecase.SignInUseCase
import com.aiinty.copayment.domain.usecase.SignUpUseCase
import com.aiinty.copayment.domain.usecase.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.VerifyOTPUseCase
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.model.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val signInUseCase: SignInUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase,
    private val recoverUseCase: RecoverUseCase,
    private val updateUserUseCase: UpdateUserUseCase
): ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationRoute>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _resendCooldownSeconds = MutableStateFlow(0)
    val resendCooldownSeconds: StateFlow<Int> = _resendCooldownSeconds.asStateFlow()
    val canResend = MutableStateFlow(true)

    private val _errorEvent = MutableSharedFlow<UiMessage>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun showError(@StringRes resId: Int? = null, message: String? = null) {
        viewModelScope.launch {
            when {
                resId != null -> _errorEvent.emit(UiMessage.StringRes(resId))
                message != null -> _errorEvent.emit(UiMessage.Text(message))
            }
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
                        _navigationEvent.emit(NavigationRoute.CreatePinCodeScreen)
                    } else {
                        _navigationEvent.emit(NavigationRoute.PinCodeScreen)
                    }
                    return@launch
                }
            }

            when {
                !firstLaunch -> {
                    _navigationEvent.emit(NavigationRoute.SignInScreen)
                }
                else -> {
                    _navigationEvent.emit(NavigationRoute.OnboardingScreen)
                }
            }
            if (firstLaunch) {
                userPreferences.saveFirstLaunch(false)
            }
        }
    }

    private fun handleError(e: Throwable) {
        viewModelScope.launch {
            when (e) {
                is IOException -> {
                    Log.e("AuthViewModel:handleError", "Network error or timeout: ${e.message}")
                    showError(resId = R.string.check_your_internet)
                }
                is ApiException -> {
                    when(e.apiError.error_code) {
                        ApiErrorCode.EMAIL_NOT_CONFIRMED.code -> {
                            val email = userPreferences.getUserEmail()
                            if (email != null) {
                                _navigationEvent.emit(NavigationRoute.VerifyOTPScreen(
                                    type = OTPType.EMAIL,
                                    email = email,
                                    nextDestination = NavigationRoute.HomeScreen.route
                                ))
                            } else {
                                showError(resId = R.string.try_signing_in_again)
                            }
                        }
                        ApiErrorCode.USER_ALREADY_EXISTS.code -> {
                            showError(resId = R.string.email_is_already_taken)
                        }
                        ApiErrorCode.OTP_EXPIRED.code -> {
                            showError(resId = R.string.token_has_expired)
                        }
                        ApiErrorCode.VALIDATION_FAILED.code -> {
                            showError(resId = R.string.verify_the_accuracy)
                        }
                        ApiErrorCode.INVALID_CREDENTIALS.code -> {
                            showError(resId = R.string.invalid_credentials)
                        }
                        ApiErrorCode.OVER_EMAIL_SEND_RATE_LIMIT.code -> {
                            showError(resId = R.string.send_rate_limit)
                        }
                        ApiErrorCode.WEAK_PASSWORD.code -> {
                            showError(resId = R.string.weak_password)
                        }
                        ApiErrorCode.BAD_JWT.code -> {
                            val refreshToken = userPreferences.getRefreshToken()
                            if (refreshToken == null) {
                                _navigationEvent.emit(NavigationRoute.SignInScreen)
                                showError(resId = R.string.try_signing_in_again)
                            } else {
                                refreshToken(refreshToken)
                            }
                        }
                        ApiErrorCode.REFRESH_TOKEN_NOT_FOUND.code -> {
                            _navigationEvent.emit(NavigationRoute.SignInScreen)
                            showError(resId = R.string.try_signing_in_again)
                        }
                        else -> {
                            Log.e("AuthViewModel:handleError", "Unknown API error: code=${e.apiError.error_code}, message=${e.message}")
                            if (e.message != null) showError(message = e.message)
                            else showError(resId = R.string.unknown_api_error)
                        }
                    }
                }
                else -> {
                    Log.e("AuthViewModel:handleError", "Unknown error: ${e.message ?: e.toString()}")
                    showError(resId = R.string.unknown_error)
                }
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
                        _navigationEvent.emit(NavigationRoute.CreatePinCodeScreen)
                    } else {
                        _navigationEvent.emit(NavigationRoute.PinCodeScreen)
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
                    _navigationEvent.emit(NavigationRoute.VerifyOTPScreen(
                        type = OTPType.EMAIL,
                        email = email,
                        nextDestination = NavigationRoute.CreatePinCodeScreen.route
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
                    _navigationEvent.emit(NavigationRoute.NextScreen)
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
                    _navigationEvent.emit(NavigationRoute.VerifyOTPScreen(
                        type = OTPType.RECOVERY,
                        email = email,
                        nextDestination = NavigationRoute.PasswordChangeScreen.route
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
                    userPreferences.clear()
                    _navigationEvent.emit(NavigationRoute.SignInScreen)
                },
                onFailure = { e ->
                    handleError(e)
                }
            )
        }
    }

    fun createPin(pin: String) {
        viewModelScope.launch {
            if (pin.length == 5 && pin.all { it.isDigit() }) {
                userPreferences.saveUserPin(pin)
                _navigationEvent.emit(NavigationRoute.HomeScreen)
            } else {
                showError(resId = R.string.error_invalid_pin_format)
            }
        }
    }

    fun loginWithPin(pin: String) {
        viewModelScope.launch {
            val storedPin = userPreferences.getUserPin()
            if (storedPin == null) {
                _navigationEvent.emit(NavigationRoute.CreatePinCodeScreen)
            } else if (pin == storedPin) {
                _navigationEvent.emit(NavigationRoute.HomeScreen)
            } else {
                showError(resId = R.string.error_invalid_pin)
            }
        }
    }
}
