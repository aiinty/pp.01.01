package com.aiinty.copayment.presentation.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.auth.createPinCodeScreen
import com.aiinty.copayment.presentation.ui.auth.onboardingScreen
import com.aiinty.copayment.presentation.ui.auth.pinCodeScreen
import com.aiinty.copayment.presentation.ui.auth.signInScreen
import com.aiinty.copayment.presentation.ui.auth.signUpScreen
import com.aiinty.copayment.presentation.ui.auth.splashScreen

fun NavGraphBuilder.authGraph(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus
) {
    navigation(
        route = NavigationGraph.AuthGraph.route,
        startDestination = NavigationRoute.SplashScreen.route
    ) {
        splashScreen(modifier)
        onboardingScreen(modifier, navigationEventBus)
        signInScreen(modifier)
        signUpScreen(modifier)
        createPinCodeScreen(modifier)
        pinCodeScreen(modifier)
    }
}