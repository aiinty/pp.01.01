package com.aiinty.copayment.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiinty.copayment.presentation.ui.screen.main.activityScreen
import com.aiinty.copayment.presentation.ui.screen.main.cardsScreen
import com.aiinty.copayment.presentation.ui.screen.main.homeScreen
import com.aiinty.copayment.presentation.ui.screen.main.profileScreen
import com.aiinty.copayment.presentation.ui.screen.main.qrCodeScreen

fun NavGraphBuilder.mainGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    homeScreen(
        modifier = modifier,
    )

    cardsScreen(
        modifier = modifier,
    )

    qrCodeScreen(
        modifier = modifier,
    )

    activityScreen(
        modifier = modifier,
    )

    profileScreen(
        modifier = modifier,
    )
}