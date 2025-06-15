package com.aiinty.copayment.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.aiinty.copayment.presentation.ui.auth.createPinCodeScreen
import com.aiinty.copayment.presentation.ui.auth.passwordChangeScreen
import com.aiinty.copayment.presentation.ui.auth.pinCodeScreen
import com.aiinty.copayment.presentation.ui.auth.recoverScreen
import com.aiinty.copayment.presentation.ui.auth.signInScreen
import com.aiinty.copayment.presentation.ui.auth.signUpScreen
import com.aiinty.copayment.presentation.ui.auth.verifyOTPScreen
import com.aiinty.copayment.presentation.ui.auth.onboardingScreen
import com.aiinty.copayment.presentation.ui.auth.splashScreen

fun NavGraphBuilder.authGraph(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus
) {
    splashScreen(modifier)
    onboardingScreen(modifier, navigationEventBus)
    signInScreen(modifier)
    signUpScreen(modifier)
    verifyOTPScreen(modifier)
    recoverScreen(modifier)
    passwordChangeScreen(modifier)
    createPinCodeScreen(modifier)
    pinCodeScreen(modifier)
}