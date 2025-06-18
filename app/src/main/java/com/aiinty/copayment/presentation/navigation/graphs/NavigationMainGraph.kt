package com.aiinty.copayment.presentation.navigation.graphs

import androidx.compose.ui.Modifier
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
import com.aiinty.copayment.presentation.ui.main.home.selectCardScreen
import com.aiinty.copayment.presentation.ui.main.home.transactionsScreen
import com.aiinty.copayment.presentation.ui.main.home.transfer.transferAmountScreen
import com.aiinty.copayment.presentation.ui.main.home.transfer.transferSelectScreen
import com.aiinty.copayment.presentation.ui.main.home.transfer.transferSuccessScreen
import com.aiinty.copayment.presentation.ui.main.home.transactionScreen
import com.aiinty.copayment.presentation.ui.main.profile.contactScreen
import com.aiinty.copayment.presentation.ui.main.profile.editProfileScreen
import com.aiinty.copayment.presentation.ui.main.profile.profileScreen
import com.aiinty.copayment.presentation.ui.main.qr.qrCodeScreen

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
        selectCardScreen(modifier, navController)
        transferSelectScreen(modifier, navController)
        transferAmountScreen(modifier, navController)
        transferSuccessScreen(modifier, navController)
        transactionScreen(modifier, navController)

        cardsScreen(modifier, navController)
        createCardOnboardingScreen(modifier, navigationEventBus)
        createCardStyleScreen(modifier, navigationEventBus)
        createCardScreen(modifier, navController)
        editCardScreen(modifier, navController)

        qrCodeScreen(modifier, navController)

        activityScreen(modifier)

        profileScreen(modifier, navController)
        editProfileScreen(modifier, navController)
        contactScreen(modifier, navController)
    }
}
