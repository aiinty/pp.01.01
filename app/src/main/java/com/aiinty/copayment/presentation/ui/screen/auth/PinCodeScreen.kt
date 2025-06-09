package com.aiinty.copayment.presentation.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.model.KeyboardInputType
import com.aiinty.copayment.presentation.navigation.CollectNavigationEvents
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.auth.AuthErrorHandler
import com.aiinty.copayment.presentation.ui.components.auth.PinCodeInput
import com.aiinty.copayment.presentation.ui.components.base.BaseButton
import com.aiinty.copayment.presentation.ui.components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui.components.base.NumericKeyboard
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun PinCodeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToCreatePinCode: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToCreatePinCode = onNavigateToCreatePinCode,
        onNavigateToHome = onNavigateToHome
    )
    AuthErrorHandler(viewModel = viewModel)

    val pin = remember { mutableStateOf("") }
    val isValidatedInputs = pin.value.length == 5

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.pin_title),
                style = Typography.titleMedium
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PinCodeInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    pin = pin
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            BaseButton(
                onClick = {
                    viewModel.loginWithPin(pin.value)
                },
                enabled = isValidatedInputs
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
            }

            NumericKeyboard(
                input = pin,
                validateRules = { input ->
                    input.length < 5
                },
                keyboardInputType = KeyboardInputType.ONLY_NUMBERS
            )
        }
    }
}

fun NavController.navigateToPinCode(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.PinCodeScreen.route, navOptions)

fun NavGraphBuilder.pinCodeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCreatePinCode: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    composable(
        route = NavigationRoute.PinCodeScreen.route
    ){
        PinCodeScreen(
            modifier = modifier,
            onNavigateToCreatePinCode = onNavigateToCreatePinCode,
            onNavigateToHome = onNavigateToHome,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PinCodeScreenPreview() {
    PinCodeScreen(
        Modifier.fillMaxSize()
    )
}
