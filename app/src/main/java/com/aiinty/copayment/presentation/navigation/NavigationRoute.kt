package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.domain.model.OTPType
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute(val route: String) {
    object SplashScreen: NavigationRoute("splash_screen")
    object OnboardingScreen: NavigationRoute("onboarding_screen")
    object SignUpScreen: NavigationRoute("sign_up")
    object SignInScreen: NavigationRoute("sign_in_screen")

    data class VerifyOTPScreen(
        val type: OTPType,
        val email: String,
        val nextDestination: String? = null
    ) : NavigationRoute(
        route = NavigationUtils.buildRoute(
            routeBase = "verify_otp/${type.code}/$email",
            params = mapOf("next" to nextDestination)
        )
    )
    object NextScreen: NavigationRoute("next")

    object RecoverScreen: NavigationRoute("recover")
    object PasswordChangeScreen: NavigationRoute("password_change")
    object CreatePinCodeScreen: NavigationRoute("create_pin_code")
    object PinCodeScreen: NavigationRoute("pin_code")
    object HomeScreen: NavigationRoute("home")
}