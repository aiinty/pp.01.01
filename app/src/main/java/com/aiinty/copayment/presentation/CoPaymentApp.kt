package com.aiinty.copayment.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiinty.copayment.presentation.ui.SplashScreenRoute
import com.aiinty.copayment.presentation.ui.navigateToOnboarding
import com.aiinty.copayment.presentation.ui.navigateToSplash
import com.aiinty.copayment.presentation.ui.onboardingScreen
import com.aiinty.copayment.presentation.ui.splashScreen

@Composable
fun CoPaymentApp(
    modifier: Modifier = Modifier
){
    Scaffold(
        modifier = modifier,
        containerColor = Color.White
    ) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SplashScreenRoute,
            modifier = modifier.padding(innerPadding)
        ) {
            splashScreen(
                onNavigateToOnboarding = { navController.navigateToOnboarding(
                    navOptions = {
                        popUpTo(SplashScreenRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                ) }
            )

            onboardingScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CoPaymentApp(
        modifier = Modifier.fillMaxSize()
    )
}
