package com.aiinty.copayment.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.SignUpData
import com.aiinty.copayment.data.network.ApiErrorCode
import com.aiinty.copayment.data.network.ApiException
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.usecase.RecoverUseCase
import com.aiinty.copayment.domain.usecase.SignInUseCase
import com.aiinty.copayment.domain.usecase.SignUpUseCase
import com.aiinty.copayment.domain.usecase.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.VerifyOTPUseCase
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val signInUseCase: SignInUseCase,
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

    fun signIn(email:String, password:String) {
        //Saving in preferences because there is no endpoint for resending the email confirmation
        userPreferences.saveUserPassword(password)
        viewModelScope.launch {
            val result = signInUseCase.invoke(email, password)
            result.fold(
                onSuccess = {
                    _navigationEvent.emit(NavigationRoute.HomeScreen)
                },
                onFailure = { e ->
                    when(e) {
                        is ApiException -> {
                            if (e.apiError.error_code == ApiErrorCode.EMAIL_NOT_CONFIRMED.code) {
                                _navigationEvent.emit(NavigationRoute.VerifyOTPScreen(
                                    type = OTPType.EMAIL,
                                    email = email,
                                    nextDestination = NavigationRoute.HomeScreen.route
                                ))
                            }
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }

    fun signUp(fullName: String, email: String, password: String) {
        //Saving in preferences because there is no endpoint for resending the email confirmation
        userPreferences.saveUserPassword(password)
        viewModelScope.launch {
            val data = SignUpData(full_name = fullName)
            val result = signUpUseCase.invoke(email = email, password = password, data = data)
            result.fold(
                onSuccess = {
                    _navigationEvent.emit(NavigationRoute.VerifyOTPScreen(
                        type = OTPType.EMAIL,
                        email = email,
                        nextDestination = NavigationRoute.HomeScreen.route
                    ))
                },
                onFailure = { e ->
                    when(e) {
                        is ApiException -> {
                            if (e.apiError.error_code == ApiErrorCode.EMAIL_NOT_CONFIRMED.code) {
                                _navigationEvent.emit(NavigationRoute.VerifyOTPScreen(
                                    type = OTPType.EMAIL,
                                    email = email,
                                    nextDestination = NavigationRoute.OnboardingScreen.route
                                ))
                            }
                            if (e.apiError.error_code == ApiErrorCode.USER_ALREADY_EXISTS.code) {
                                _navigationEvent.emit(NavigationRoute.SplashScreen)
                            }
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }

    fun verifyOTP(type: OTPType, email: String, token: String) {
        viewModelScope.launch {
            val result = verifyOTPUseCase.invoke(type, email, token)
            result.fold(
                onSuccess = {
                    _navigationEvent.emit(NavigationRoute.HomeScreen)
                },
                onFailure = { e ->
                    when(e) {
                        is ApiException -> {
                            if (e.apiError.error_code == ApiErrorCode.OTP_EXPIRED.code) {

                            }
                            if (e.apiError.error_code == ApiErrorCode.VALIDATION_FAILED.code) {

                            }
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }

    fun resendOTP(type: OTPType, email: String) {
        if (!canResend.value) return
        canResend.value = false
        viewModelScope.launch {
            try {
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
                    onFailure = {
                        startResendCooldown(60)
                    }
                )
            } catch (e: Exception) {

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
                    when(e) {
                        is ApiException -> {

                        }
                        else -> {

                        }
                    }
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
                    when(e) {
                        is ApiException -> {

                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }
}
