package com.aiinty.copayment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import com.aiinty.copayment.R

data class TopBarState(
    val titleResId: Int? = null,
    val showBackButton: Boolean = true,
    val actionIcon: ImageVector? = null,
    val actionIconContentDescriptionResId: Int? = null,
    val onActionClick: suspend () -> Unit = {},
    val onBackClick: suspend () -> Unit = {},
    val isVisible: Boolean = true
)

@Composable
fun rememberTopBarState(
    currentNavigationRoute: NavigationRoute?,
    navigationEventBus: NavigationEventBus
): TopBarState {
    return remember(currentNavigationRoute) {
        val defaultBackClick: suspend () -> Unit = {
            navigationEventBus.send(NavigationEvent.Back)
        }

        if (currentNavigationRoute == null || currentNavigationRoute in listOf(
                NavigationRoute.SplashScreen,
                NavigationRoute.OnboardingScreen,
                NavigationRoute.PinCodeScreen,
                NavigationRoute.CreateCardOnboardingScreen,
                NavigationRoute.HomeScreen,
                NavigationRoute.TransactionsScreen
            )
        ) {
            return@remember TopBarState(isVisible = false)
        }

        val titleResIdMap = mapOf(
            NavigationRoute.SelectCardScreen to R.string.select_your_card,
            NavigationRoute.TransferSelectScreen to R.string.transfer,
            NavigationRoute.TransferAmountScreen to R.string.send_money,

            NavigationRoute.CardsScreen to R.string.my_card,
            NavigationRoute.CreateCardStyleScreen to R.string.create_card_style_title,
            NavigationRoute.CreateCardScreen() to R.string.create_card_title,
            NavigationRoute.EditCardScreen to R.string.edit_card,

            NavigationRoute.QRCodeScreen to R.string.show_qr_code,

            NavigationRoute.ActivityScreen to R.string.activity,

            NavigationRoute.ProfileScreen to R.string.profile,
            NavigationRoute.EditProfileScreen to R.string.edit_profile,
            NavigationRoute.ContactScreen to R.string.contacts,
        )

        TopBarState(
            titleResId = titleResIdMap[currentNavigationRoute],
            showBackButton = true,
            onBackClick = defaultBackClick
        )
    }
}
