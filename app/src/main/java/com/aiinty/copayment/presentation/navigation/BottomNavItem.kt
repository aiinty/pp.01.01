package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.R

sealed class BottomNavItem(
    val route: String,
    val iconResId: Int? = null,
    val label: String
) {
    data object Home : BottomNavItem(
        route = NavigationRoute.HomeScreen.route,
        iconResId = R.drawable.home,
        label = "Home"
    )

    data object Cards : BottomNavItem(
        route =NavigationRoute.CardsScreen.route,
        iconResId = R.drawable.cards,
        label = "My Card"
    )

    data object QRCode : BottomNavItem(
        route = NavigationRoute.QRCodeScreen.route,
        iconResId = R.drawable.qr_code,
        label = "Scan")

    data object Activity : BottomNavItem(
        route = NavigationRoute.ActivityScreen.route,
        iconResId = R.drawable.activity,
        label = "Activity")

    data object Profile : BottomNavItem(
        route = NavigationRoute.ProfileScreen.route,
        iconResId = R.drawable.profile,
        label = "Profile"
    )
}