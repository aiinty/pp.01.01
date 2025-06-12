package com.aiinty.copayment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun CoPaymentNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoute.SplashScreen.route
) {
    //FIXME: NavOptions where its necessary
    //FIXME: Fix backstack growth with navigation
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        authGraph(navController)

        mainGraph(navController)
    }
}
