package com.aiinty.copayment.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.model.KeyboardInputType
import com.aiinty.copayment.presentation.navigation.CollectNavigationEvents
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.auth.PinCodeInput
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.NumericKeyboard
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun CreatePinCodeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    nextDestination: String = "",
    onNavigateToNext: (String) -> Unit
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToNext = { onNavigateToNext(nextDestination) }
    )
    UiErrorHandler(viewModel = viewModel)

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

fun NavController.navigateToCreatePinCode(
    nextDestination: String? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) =
    navigate(route = NavigationRoute.CreatePinCodeScreen(nextDestination).route, navOptions)

fun NavGraphBuilder.createPinCodeScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onNavigateToNext: (String) -> Unit
) {
    composable(
        route = "${NavigationRoute.CreatePinCodeScreen().route}?next={next}",
        arguments = listOf(
            navArgument("next") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )
    ){ backStackEntry ->
        val nextDestination = backStackEntry.arguments?.getString("next")
        if (nextDestination != null) {
            CreatePinCodeScreen(
                modifier = modifier,
                nextDestination = nextDestination,
                onNavigateToNext = onNavigateToNext
            )
        } else {
            onNavigateToBack()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreatePinCodeScreenPreview() {
    CreatePinCodeScreen(
        Modifier.fillMaxSize(),
        onNavigateToNext = {}
    )
}
