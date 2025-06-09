package com.aiinty.copayment.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.CollectNavigationEvents
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.auth.AuthErrorHandler
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToCreatePinCode: () -> Unit = {},
    onNavigateToPinCode: () -> Unit = {},
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToOnboarding = onNavigateToOnboarding,
        onNavigateToSignIn = onNavigateToSignIn,
        onNavigateToCreatePinCode = onNavigateToCreatePinCode,
        onNavigateToPinCode = onNavigateToPinCode
    )
    AuthErrorHandler(viewModel = viewModel)

    LaunchedEffect(Unit) {
        viewModel.startSplashLogic()
    }

    Column(
        modifier = modifier
            .background(Green),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.splashlogo),
            contentDescription = stringResource(R.string.logo)
        )
        Row {
            Text(
                text = stringResource(R.string.splash_title_part_1),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.Black
            )
            Text(
                text = stringResource(R.string.splash_title_part_2),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White
            )
        }
    }
}

fun NavController.navigateToSplash(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.SplashScreen.route, navOptions)

fun NavGraphBuilder.splashScreen(
    modifier: Modifier = Modifier,
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToCreatePinCode: () -> Unit = {},
    onNavigateToPinCode: () -> Unit = {},
    ) {
    composable(
        route = NavigationRoute.SplashScreen.route
    ) {
        SplashScreen(
            modifier = modifier,
            onNavigateToOnboarding = onNavigateToOnboarding,
            onNavigateToSignIn = onNavigateToSignIn,
            onNavigateToCreatePinCode = onNavigateToCreatePinCode,
            onNavigateToPinCode = onNavigateToPinCode,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        Modifier.fillMaxSize()
    )
}
