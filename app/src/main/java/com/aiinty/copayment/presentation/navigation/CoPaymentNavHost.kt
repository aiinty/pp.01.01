package com.aiinty.copayment.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun CoPaymentNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigationManager: NavigationManager,
    startDestination: String = NavigationRoute.SplashScreen.route,
) {
    val isBlocked = remember { mutableStateOf(false) }
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val animationDuration = 500

    LaunchedEffect(Unit) {
        navigationManager.events.collect { event ->
            when (event) {
                is NavigationEvent.Navigate -> if (!isBlocked.value) {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateUp -> if (!isBlocked.value) {
                    navController.navigateUp()
                }
                is NavigationEvent.BlockNavigation -> isBlocked.value = true
                is NavigationEvent.UnblockNavigation -> isBlocked.value = false
            }
        }
    }

    LaunchedEffect(currentBackStackEntry) {
        navigationManager.block()
        kotlinx.coroutines.delay(animationDuration.toLong())
        navigationManager.unblock()
    }

    //FIXME: NavOptions where its necessary
    //FIXME: Fix backstack growth with navigation
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(animationDuration)) },
        exitTransition = { fadeOut(tween(animationDuration)) }
    ) {
        authGraph(
            navController = navController
        )

        mainGraph(
            navController = navController
        )
    }
}
