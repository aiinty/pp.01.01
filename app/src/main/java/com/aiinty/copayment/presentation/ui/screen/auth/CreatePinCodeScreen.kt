package com.aiinty.copayment.presentation.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun CreatePinCodeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToHome = onNavigateToHome
    )
    AuthErrorHandler(viewModel = viewModel)

    val firstPin = remember { mutableStateOf<String?>(null) }
    val isConfirmStep = firstPin.value != null
    val pin = remember { mutableStateOf("") }
    val isValidatedInputs = pin.value.length == 5
    val showError = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            BaseIconButton(
                onClick = onNavigateToBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left),
                    contentDescription = stringResource(R.string.back)
                )
            }

            CreatePinCodeHeader(
                isConfirmStep = isConfirmStep
            )

            PinCodeInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                pin = pin
            )

            if (showError.value) {
                Text(
                    text = stringResource(R.string.error_match_pins),
                    color = Color.Red,
                    style = Typography.bodyMedium
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            BaseButton(
                onClick = {
                    if (firstPin.value == null) {
                        showError.value = false
                        firstPin.value = pin.value
                        pin.value = ""
                    } else {
                        if (firstPin.value == pin.value) {
                            viewModel.createPin(pin.value)
                        } else {
                            showError.value = true
                            pin.value = ""
                            firstPin.value = null
                        }
                    }
                },
                enabled = isValidatedInputs
            ) {
                Text(
                    text = if (firstPin.value == null) stringResource(R.string.next) else
                        stringResource(R.string.create_pin_confirm),
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

@Composable
private fun CreatePinCodeHeader(
    modifier: Modifier = Modifier,
    isConfirmStep: Boolean
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val titleText = if (isConfirmStep) {
            stringResource(R.string.create_pin_comfirm_title)
        } else {
            stringResource(R.string.create_pin_title)
        }
        val descText = if (isConfirmStep) {
            stringResource(R.string.create_pin_cofirm_desc)
        } else {
            stringResource(R.string.create_pin_desc)
        }

        Text(
            text = titleText,
            style = Typography.titleMedium
        )
        Text(
            text = descText,
            style = Typography.bodyMedium
        )
    }
}

fun NavController.navigateToCreatePinCode(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.CreatePinCodeScreen.route, navOptions)

fun NavGraphBuilder.createPinCodeScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    composable(
        route = NavigationRoute.CreatePinCodeScreen.route
    ){
        CreatePinCodeScreen(
            modifier = modifier,
            onNavigateToBack = onNavigateToBack,
            onNavigateToHome = onNavigateToHome
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreatePinCodeScreenPreview() {
    CreatePinCodeScreen(
        Modifier.fillMaxSize()
    )
}
