package com.aiinty.copayment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.presentation.ui.homeScreen
import com.aiinty.copayment.presentation.ui.navigateToHome
import com.aiinty.copayment.presentation.ui.navigateToOnboarding
import com.aiinty.copayment.presentation.ui.auth.navigateToRecover
import com.aiinty.copayment.presentation.ui.auth.navigateToSignIn
import com.aiinty.copayment.presentation.ui.auth.navigateToSignUp
import com.aiinty.copayment.presentation.ui.auth.navigateToVerifyOTP
import com.aiinty.copayment.presentation.ui.onboardingScreen
import com.aiinty.copayment.presentation.ui.auth.passwordChangeScreen
import com.aiinty.copayment.presentation.ui.auth.recoverScreen
import com.aiinty.copayment.presentation.ui.auth.signInScreen
import com.aiinty.copayment.presentation.ui.auth.signUpScreen
import com.aiinty.copayment.presentation.ui.splashScreen
import com.aiinty.copayment.presentation.ui.auth.verifyOTPScreen

@Composable
fun CoPaymentNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoute.SplashScreen.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        splashScreen(
            onNavigateToOnboarding = { navController.navigateToOnboarding(
                navOptions = {
                    popUpTo(NavigationRoute.SplashScreen.route) { inclusive = true }
                    launchSingleTop = true
                })
            }
        )

        onboardingScreen(
            onNavigateToSignIn = { navController.navigateToSignIn() }
        )

        signInScreen(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToForgotPassword = { navController.navigateToRecover() },
            onNavigateToSignUp = { navController.navigateToSignUp() },
            onNavigateToVerify = { otpType: OTPType, email: String, nextDestination: String? ->
                navController.navigateToVerifyOTP(otpType, email, nextDestination)
            },
            onNavigateToHome = { navController.navigateToHome() }
        )

        signUpScreen(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToSignIn = { navController.navigateToSignIn() },
            onNavigateToVerify = { otpType: OTPType, email: String, nextDestination: String? ->
                navController.navigateToVerifyOTP(otpType, email, nextDestination)
            }
        )

        verifyOTPScreen(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToNext = { nextDestination ->
                navController.navigate(nextDestination)
            }
        )

        recoverScreen(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToVerify = { otpType: OTPType, email: String, nextDestination: String? ->
                navController.navigateToVerifyOTP(otpType, email, nextDestination)
            }
        )

        passwordChangeScreen(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToSignIn = { navController.navigateToSignIn(
                navOptions = {
                    popUpTo(NavigationRoute.PasswordChangeScreen.route) { inclusive = true }
                    launchSingleTop = true
                })
            }
        )

        homeScreen(
            onNavigateToBack = { navController.popBackStack() },
        )
    }
}
