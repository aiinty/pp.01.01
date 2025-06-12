package com.aiinty.copayment.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiinty.copayment.presentation.ui.screen.main.activityScreen
import com.aiinty.copayment.presentation.ui.screen.main.cardsScreen
import com.aiinty.copayment.presentation.ui.screen.main.homeScreen
import com.aiinty.copayment.presentation.ui.screen.main.profileScreen
import com.aiinty.copayment.presentation.ui.screen.main.qrCodeScreen

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    homeScreen(
        onNavigateToBack = { navController.popBackStack() },
    )

    cardsScreen(
        onNavigateToBack = { navController.popBackStack() },
    )

    qrCodeScreen(
        onNavigateToBack = { navController.popBackStack() },
    )

    activityScreen(
        onNavigateToBack = { navController.popBackStack() },
    )

    profileScreen(
        onNavigateToBack = { navController.popBackStack() },
    )
}