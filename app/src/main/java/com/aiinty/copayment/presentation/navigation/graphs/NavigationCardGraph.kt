package com.aiinty.copayment.presentation.navigation.graphs

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.main.card.cardsScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardOnboardingScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardStyleScreen
import com.aiinty.copayment.presentation.ui.main.card.editCardScreen

fun NavGraphBuilder.cardGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationEventBus: NavigationEventBus
) {
    navigation(
        route = NavigationGraph.CardGraph.route,
        startDestination = NavigationRoute.CreateCardOnboardingScreen.route
    ) {
        cardsScreen(modifier, navController)
        createCardOnboardingScreen(modifier, navigationEventBus)
        createCardStyleScreen(modifier, navigationEventBus)
        createCardScreen(modifier, navController)
        editCardScreen(modifier, navController)
    }
}
