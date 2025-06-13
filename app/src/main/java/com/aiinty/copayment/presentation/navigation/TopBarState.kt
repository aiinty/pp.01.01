package com.aiinty.copayment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class TopBarState(
    val title: String? = null,
    val showBackButton: Boolean = true,
    val actionIcon: ImageVector? = null,
    val actionIconContentDescriptionResId: Int? = null,
    val onActionClick: () -> Unit = {},
    val onBackClick: () -> Unit = {},
    val isVisible: Boolean = true
)

@Composable
fun rememberTopBarState(
    currentNavigationRoute: NavigationRoute?,
    navController: NavHostController
): TopBarState {
    return remember(currentNavigationRoute) {
        val defaultBackClick: () -> Unit = { navController.popBackStack() }

        if (currentNavigationRoute == null || currentNavigationRoute in listOf(
                NavigationRoute.SplashScreen,
                NavigationRoute.OnboardingScreen,
                NavigationRoute.PinCodeScreen
            )
        ) {
            return@remember TopBarState(isVisible = false)
        }

        val titleMap = mapOf(
            NavigationRoute.CardsScreen to "My Card",
            NavigationRoute.QRCodeScreen to "Scan QR Code",
            NavigationRoute.ActivityScreen to "Activity",
            NavigationRoute.ProfileScreen to "Profile"
        )

        TopBarState(
            title = titleMap[currentNavigationRoute],
            showBackButton = true,
            onBackClick = defaultBackClick
        )
    }
}