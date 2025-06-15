package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.R

sealed class BottomNavItem(
    val route: String,
    val iconResId: Int? = null,
    val labelResId: Int? = null
) {
    data object Home : BottomNavItem(
        route = NavigationRoute.HomeScreen.route,
        iconResId = R.drawable.home,
        labelResId = R.string.home
    )

    data object Cards : BottomNavItem(
        route = NavigationRoute.CardsScreen.route,
        iconResId = R.drawable.cards,
        labelResId = R.string.my_card
    )

    data object QRCode : BottomNavItem(
        route = NavigationRoute.ShowQRCodeScreen.route,
        iconResId = R.drawable.qr_code,
        labelResId = R.string.scan
    )

    data object Activity : BottomNavItem(
        route = NavigationRoute.ActivityScreen.route,
        iconResId = R.drawable.activity,
        labelResId = R.string.activity
    )

    data object Profile : BottomNavItem(
        route = NavigationRoute.ProfileScreen.route,
        iconResId = R.drawable.profile,
        labelResId = R.string.profile
    )
}