package com.aiinty.copayment.presentation.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.main.activity.activityScreen
import com.aiinty.copayment.presentation.ui.main.card.cardsScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardOnboardingScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardStyleScreen
import com.aiinty.copayment.presentation.ui.main.card.editCardScreen
import com.aiinty.copayment.presentation.ui.main.home.homeScreen
import com.aiinty.copayment.presentation.ui.main.home.transactionsScreen
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
    navigation(
        route = NavigationGraph.MainGraph.route,
        startDestination = NavigationRoute.HomeScreen.route
    ) {
        homeScreen(modifier, navController)
        transactionsScreen(modifier, navController)

        cardsScreen(modifier, navController)
        createCardOnboardingScreen(modifier, navigationEventBus)
        createCardStyleScreen(modifier, navigationEventBus)
        createCardScreen(modifier, navController)
        editCardScreen(modifier, navController)

        showQRCodeScreen(modifier)
        scanQRCodeScreen(modifier)

        activityScreen(modifier)

        profileScreen(modifier)
        editProfileScreen(modifier)
        contactScreen(modifier)
    }
}
