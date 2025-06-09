package com.aiinty.copayment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiinty.copayment.domain.model.OTPType
import kotlinx.coroutines.flow.Flow

@Composable
fun CollectNavigationEvents(
    navigationFlow: Flow<NavigationRoute>,
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToVerify: (type: OTPType, email: String, nextDestination: String?) -> Unit =
        { _, _, _ -> },
    onNavigateToHome: () -> Unit = {},
    onNavigateToNext: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        navigationFlow.collect { target ->
            when(target) {
                is NavigationRoute.SignInScreen -> onNavigateToSignIn()
                is NavigationRoute.VerifyOTPScreen ->
                    onNavigateToVerify(target.type, target.email, target.nextDestination)
                is NavigationRoute.HomeScreen -> onNavigateToHome()
                is NavigationRoute.NextScreen -> onNavigateToNext()
                else -> {}
            }
        }
    }
}