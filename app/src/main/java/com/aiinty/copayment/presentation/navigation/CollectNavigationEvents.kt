package com.aiinty.copayment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiinty.copayment.domain.model.OTPType
import kotlinx.coroutines.flow.Flow

@Composable
fun CollectNavigationEvents(
    navigationFlow: Flow<NavigationRoute>,
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToVerify: (type: OTPType, email: String, nextDestination: String?) -> Unit =
        { _, _, _ -> },
    onNavigateToCreatePinCode : () -> Unit = {},
    onNavigateToPinCode : () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNext: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        navigationFlow.collect { target ->
            when(target) {
                is NavigationRoute.OnboardingScreen -> onNavigateToOnboarding()
                is NavigationRoute.SignInScreen -> onNavigateToSignIn()
                is NavigationRoute.VerifyOTPScreen ->
                    onNavigateToVerify(target.type, target.email, target.nextDestination)
                is NavigationRoute.CreatePinCodeScreen -> onNavigateToCreatePinCode()
                is NavigationRoute.PinCodeScreen -> onNavigateToPinCode()
                is NavigationRoute.HomeScreen -> onNavigateToHome()
                is NavigationRoute.ProfileScreen -> onNavigateToProfile()
                is NavigationRoute.NextScreen -> onNavigateToNext()
                else -> {}
            }
        }
    }
}
