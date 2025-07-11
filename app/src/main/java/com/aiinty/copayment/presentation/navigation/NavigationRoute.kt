package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.model.TransactionType
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute(
    val route: String,
    val showBottomBar: Boolean = false,
    ) {
    data object NextScreen: NavigationRoute("next")
    data object SplashScreen: NavigationRoute("splash_screen")
    data object OnboardingScreen: NavigationRoute("onboarding_screen")

    data object SignUpScreen: NavigationRoute("sign_up")
    data object SignInScreen: NavigationRoute("sign_in_screen")
    data object RecoverScreen: NavigationRoute("recover")
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
    data class PasswordChangeScreen(
        val nextDestination: String? = null
    ): NavigationRoute(
        route = NavigationUtils.buildRoute(
            routeBase = "password_change",
            params = mapOf("next" to nextDestination)
        )
    )
    data object PinCodeScreen: NavigationRoute("pin_code")
    data class CreatePinCodeScreen(
        val nextDestination: String? = null
    ): NavigationRoute(
        route = NavigationUtils.buildRoute(
            routeBase = "pin_code_create",
            params = mapOf("next" to nextDestination)
        )
    )

    data object HomeScreen: NavigationRoute("home", true)
    data object TransactionsScreen: NavigationRoute("transactions", false)
    data object SelectCardScreen: NavigationRoute("select_card", false)
    data object TransferScreen: NavigationRoute("transfer", false)
    data object TransferSelectScreen: NavigationRoute("transfer_select", false)
    data object TransferAmountScreen: NavigationRoute("transfer_amount", false)
    data object TransferProofScreen: NavigationRoute("transfer_proof", false)
    data class TransactionScreen(val type: TransactionType): NavigationRoute(
        route = "transaction/${type.id}",
        showBottomBar = false
    )

    data object CardsScreen: NavigationRoute("cards", true)
    data object CreateCardOnboardingScreen: NavigationRoute("create_card_onboarding")
    data object CreateCardStyleScreen: NavigationRoute("create_card_style")
    data class CreateCardScreen(
        val cardStyle: CardStyle = CardStyle.CLASSIC
    ): NavigationRoute(
        route = NavigationUtils.buildRoute(
            routeBase = "create_card",
            params = mapOf("style" to cardStyle.ordinal.toString())
        )
    )
    data object EditCardScreen: NavigationRoute("edit_card")

    data object QRCodeScreen: NavigationRoute("qr_code")

    data object ActivityScreen: NavigationRoute("activity", true)

    data object ProfileScreen: NavigationRoute("profile", true)
    data object ContactScreen: NavigationRoute("contacts", true)
    data object EditProfileScreen: NavigationRoute("edit_profile")

    companion object {
        private val routes: Map<String, NavigationRoute> = listOfNotNull(
            NextScreen, SplashScreen, OnboardingScreen,
            SignUpScreen, SignInScreen, RecoverScreen, PinCodeScreen,
            HomeScreen, TransactionsScreen, SelectCardScreen,
            TransferScreen, TransferSelectScreen, TransferAmountScreen, TransferProofScreen,
            CardsScreen, CreateCardOnboardingScreen, CreateCardStyleScreen, EditCardScreen,
            QRCodeScreen,
            ActivityScreen,
            ProfileScreen, EditProfileScreen, ContactScreen,
        ).associateBy { it.route }

        fun findByRoute(route: String?): NavigationRoute? {
            return when {
                route == null -> null
                route.startsWith("verify_otp/") -> VerifyOTPScreen(
                    type = OTPType.EMAIL,
                    email = "",
                    nextDestination = null
                )
                route.startsWith("password_change") -> PasswordChangeScreen()
                route.startsWith("pin_code_create") -> CreatePinCodeScreen()
                route.startsWith("create_card?") -> CreateCardScreen()
                route.startsWith("transaction/") -> TransactionScreen(TransactionType.WITHDRAW)
                else -> routes[route]
            }
        }
    }
}