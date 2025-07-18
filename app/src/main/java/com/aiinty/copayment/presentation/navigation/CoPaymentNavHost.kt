package com.aiinty.copayment.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.navigation.graphs.authGraph
import com.aiinty.copayment.presentation.navigation.graphs.mainGraph
import com.aiinty.copayment.presentation.ui.auth.passwordChangeScreen
import com.aiinty.copayment.presentation.ui.auth.recoverScreen
import com.aiinty.copayment.presentation.ui.auth.verifyOTPScreen

@Composable
fun CoPaymentNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigationEventBus: NavigationEventBus,
    startDestination: String = NavigationGraph.AuthGraph.route,
) {
    LaunchedEffect(Unit) {
        navigationEventBus.events.collect { event ->
            when (event) {
                is NavigationEvent.ToRoute -> {
                    navController.navigate(event.route) { launchSingleTop = true }
                }
                is NavigationEvent.Back -> {
                    navController.popBackStack()
                }
                is NavigationEvent.ToVerifyOTP -> {
                    val route = NavigationRoute.VerifyOTPScreen(
                        event.type,
                        event.email,
                        event.nextDestination
                    ).route
                    navController.navigate(route) { launchSingleTop = true }
                }
                is NavigationEvent.ToTransaction -> {
                    val route = NavigationRoute.TransactionScreen(event.type).route
                    navController.navigate(route) { launchSingleTop = true }
                }
            }
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(500)) },
        exitTransition = { fadeOut(tween(500)) }
    ) {
        authGraph(
            navigationEventBus = navigationEventBus
        )

        verifyOTPScreen()
        recoverScreen()
        passwordChangeScreen()

        mainGraph(
            navController = navController,
            navigationEventBus = navigationEventBus
        )
    }
}
