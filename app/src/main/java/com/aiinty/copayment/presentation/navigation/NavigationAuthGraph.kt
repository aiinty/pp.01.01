package com.aiinty.copayment.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.presentation.ui.auth.createPinCodeScreen
import com.aiinty.copayment.presentation.ui.auth.navigateToCreatePinCode
import com.aiinty.copayment.presentation.ui.auth.navigateToPinCode
import com.aiinty.copayment.presentation.ui.auth.navigateToRecover
import com.aiinty.copayment.presentation.ui.auth.navigateToSignIn
import com.aiinty.copayment.presentation.ui.auth.navigateToSignUp
import com.aiinty.copayment.presentation.ui.auth.navigateToVerifyOTP
import com.aiinty.copayment.presentation.ui.auth.passwordChangeScreen
import com.aiinty.copayment.presentation.ui.auth.pinCodeScreen
import com.aiinty.copayment.presentation.ui.auth.recoverScreen
import com.aiinty.copayment.presentation.ui.auth.signInScreen
import com.aiinty.copayment.presentation.ui.auth.signUpScreen
import com.aiinty.copayment.presentation.ui.auth.verifyOTPScreen
import com.aiinty.copayment.presentation.ui.main.navigateToHome
import com.aiinty.copayment.presentation.ui.auth.navigateToOnboarding
import com.aiinty.copayment.presentation.ui.auth.onboardingScreen
import com.aiinty.copayment.presentation.ui.auth.splashScreen

fun NavGraphBuilder.authGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    splashScreen(
        modifier = modifier,
        onNavigateToOnboarding = { navController.navigateToOnboarding(
            navOptions = {
                withPopUpTo(NavigationRoute.SplashScreen.route)
            })
        },
        onNavigateToCreatePinCode = { navController.navigateToCreatePinCode(
            navOptions = {
                withPopUpTo(NavigationRoute.SplashScreen.route)
            })
        },
        onNavigateToPinCode = { navController.navigateToPinCode(
            navOptions = {
                withPopUpTo(NavigationRoute.SplashScreen.route)
            })
        },
        onNavigateToSignIn = { navController.navigateToSignIn(
            navOptions = {
                withPopUpTo(NavigationRoute.SplashScreen.route)
            })
        },
    )

    onboardingScreen(
        modifier = modifier,
        onNavigateToSignIn = { navController.navigateToSignIn() }
    )

    signInScreen(
        modifier = modifier,
        onNavigateToForgotPassword = { navController.navigateToRecover() },
        onNavigateToSignUp = { navController.navigateToSignUp() },
        onNavigateToVerify = { otpType: OTPType, email: String, nextDestination: String? ->
            navController.navigateToVerifyOTP(otpType, email, nextDestination)
        },
        onNavigateToPinCode = { navController.navigateToPinCode() },
        onNavigateToCreatePinCode = { navController.navigateToCreatePinCode() },
        onNavigateToHome = { navController.navigateToHome() }
    )

    signUpScreen(
        modifier = modifier,
        onNavigateToSignIn = { navController.navigateToSignIn() },
        onNavigateToVerify = { otpType: OTPType, email: String, nextDestination: String? ->
            navController.navigateToVerifyOTP(otpType, email, nextDestination)
        }
    )

    verifyOTPScreen(
        modifier = modifier,
        onNavigateToNext = { nextDestination ->
            navController.navigate(nextDestination)
        }
    )

    recoverScreen(
        modifier = modifier,
        onNavigateToVerify = { otpType: OTPType, email: String, nextDestination: String? ->
            navController.navigateToVerifyOTP(otpType, email, nextDestination)
        }
    )

    passwordChangeScreen(
        modifier = modifier,
        onNavigateToNext = { nextDestination ->
            navController.navigate(
                route = nextDestination,
                navOptions = NavOptions.Builder().apply {
                    withPopUpTo(NavigationRoute.PasswordChangeScreen().route)
                }.build()
            )
        }
    )

    createPinCodeScreen(
        modifier = modifier,
        onNavigateToNext = { nextDestination ->
            navController.navigate(
                route = nextDestination,
                navOptions = NavOptions.Builder().apply {
                    withPopUpTo(NavigationRoute.CreatePinCodeScreen().route)
                }.build()
            )
        }
    )

    pinCodeScreen(
        modifier = modifier,
        onNavigateToCreatePinCode = { navController.navigateToCreatePinCode(
            navOptions = {
                withPopUpTo(NavigationRoute.PinCodeScreen.route)
            }
        ) },
        onNavigateToHome = { navController.navigateToHome() }
    )
}