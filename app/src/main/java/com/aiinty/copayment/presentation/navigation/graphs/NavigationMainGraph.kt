package com.aiinty.copayment.presentation.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.ui.main.activity.activityScreen
import com.aiinty.copayment.presentation.ui.main.home.homeScreen
import com.aiinty.copayment.presentation.ui.main.profile.contactScreen
import com.aiinty.copayment.presentation.ui.main.profile.editProfileScreen
import com.aiinty.copayment.presentation.ui.main.profile.profileScreen
import com.aiinty.copayment.presentation.ui.main.qr.scanQRCodeScreen
import com.aiinty.copayment.presentation.ui.main.qr.showQRCodeScreen

fun NavGraphBuilder.mainGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationEventBus: NavigationEventBus
) {
    homeScreen(modifier)

    cardGraph(
        modifier = modifier,
        navController = navController,
        navigationEventBus = navigationEventBus
    )

    showQRCodeScreen(modifier)
    scanQRCodeScreen(modifier)

    activityScreen(modifier)

    profileScreen(modifier)
    editProfileScreen(modifier)
    contactScreen(modifier)
}
