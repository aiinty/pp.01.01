package com.aiinty.copayment.presentation.common

import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.network.ApiErrorCode
import com.aiinty.copayment.data.network.ApiException
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import java.io.IOException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val userPreferences: UserPreferences,
) {
    suspend fun handle(
        throwable: Throwable,
        onNavigate: suspend (NavigationRoute) -> Unit,
        onErrorMessage: suspend (UiMessage) -> Unit
    ) {
        when (throwable) {
            is IOException -> {
                onErrorMessage(UiMessage.StringRes(R.string.check_your_internet))
            }
            is AppException -> {
                when (throwable) {
                        is AppException.UiTextError -> onErrorMessage(UiMessage.Text(throwable.userMessage))
                    is AppException.UiResError -> onErrorMessage(UiMessage.StringRes(throwable.resId))
                }
            }
            is ApiException -> {
                when (throwable.apiError.error_code) {
                    ApiErrorCode.EMAIL_NOT_CONFIRMED.code -> {
                        val email = userPreferences.getUserEmail()
                        if (email != null) {
                            onNavigate(NavigationRoute.VerifyOTPScreen(
                                type = OTPType.EMAIL,
                                email = email,
                                nextDestination = NavigationRoute.HomeScreen.route
                            ))
                        } else {
                            onErrorMessage(UiMessage.StringRes(R.string.try_signing_in_again))
                        }
                    }
                    ApiErrorCode.USER_ALREADY_EXISTS.code -> {
                        onErrorMessage(UiMessage.StringRes(R.string.email_is_already_taken))
                    }
                    ApiErrorCode.OTP_EXPIRED.code -> {
                        onErrorMessage(UiMessage.StringRes(R.string.token_has_expired))
                    }
                    ApiErrorCode.INVALID_CREDENTIALS.code -> {
                        onErrorMessage(UiMessage.StringRes(R.string.invalid_credentials))
                    }
                    ApiErrorCode.BAD_JWT.code -> {
                        onNavigate(NavigationRoute.SignInScreen)
                        onErrorMessage(UiMessage.StringRes(R.string.try_signing_in_again))
                    }
                    else -> {
                        if (throwable.message != null) {
                            onErrorMessage(UiMessage.Text(throwable.message.toString()))
                        } else {
                            onErrorMessage(UiMessage.StringRes(R.string.unknown_api_error))
                        }
                    }
                }
            }
            else -> {
                onErrorMessage(UiMessage.StringRes(R.string.unknown_error))
            }
        }
    }
}
