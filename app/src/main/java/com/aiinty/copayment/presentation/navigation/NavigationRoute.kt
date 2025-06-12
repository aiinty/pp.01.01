package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.domain.model.OTPType
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute(
    val route: String,
    val showBottomBar: Boolean = false,
    ) {
    data object SplashScreen: NavigationRoute("splash_screen")
    data object OnboardingScreen: NavigationRoute("onboarding_screen")
    data object SignUpScreen: NavigationRoute("sign_up")
    data object SignInScreen: NavigationRoute("sign_in_screen")

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
    data object NextScreen: NavigationRoute("next")

    data object RecoverScreen: NavigationRoute("recover")
    data object PasswordChangeScreen: NavigationRoute("password_change")
    data object CreatePinCodeScreen: NavigationRoute("create_pin_code")
    data object PinCodeScreen: NavigationRoute("pin_code")
    data object HomeScreen: NavigationRoute("home", true)
    data object CardsScreen: NavigationRoute("cards", true)
    data object QRCodeScreen: NavigationRoute("qr_code")
    data object ActivityScreen: NavigationRoute("activity", true)
    data object ProfileScreen: NavigationRoute("profile", true)

    companion object {
        val routes: Map<String, NavigationRoute> = listOfNotNull(
            SplashScreen, OnboardingScreen, SignUpScreen, SignInScreen, NextScreen,
            RecoverScreen, PasswordChangeScreen, CreatePinCodeScreen, PinCodeScreen,
            HomeScreen, CardsScreen, QRCodeScreen, ActivityScreen, ProfileScreen
        ).associateBy { it.route }
    }
}