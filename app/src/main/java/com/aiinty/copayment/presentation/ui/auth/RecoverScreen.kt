package com.aiinty.copayment.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.presentation.navigation.CollectNavigationEvents
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.auth.EmailTextField
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun RecoverScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToVerify: (OTPType, String, String?) -> Unit = { _, _, _ -> }
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToVerify = { type, email, nextDestination ->
            onNavigateToVerify(type, email, nextDestination)
        }
    )
    UiErrorHandler(viewModel = viewModel)

    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf<Int?>(null) }
    val isInputsValidated = emailError.value == null && email.value.isNotEmpty()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            RecoverHeader()

            RecoverFields(
                email = email,
                emailError = emailError
            )
        }

        BaseButton(
            onClick = {
                if (isInputsValidated) {
                    viewModel.recover(email.value)
                }
            },
            enabled = isInputsValidated
        ) {
            Text(
                text = stringResource(R.string.send_me_email),
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                color = Color.White
            )
        }
    }
}

@Composable
private fun RecoverHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.recovery_title),
            style = Typography.titleMedium
        )
        Text(
            text = stringResource(R.string.recovery_desc),
            style = Typography.bodyMedium
        )
    }
}

@Composable
private fun RecoverFields(
    modifier: Modifier = Modifier,
    email: MutableState<String>,
    emailError: MutableState<Int?>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EmailTextField(
            modifier = Modifier.fillMaxWidth(),
            email = email.value,
            onEmailChange = { email.value = it },
            onValidationResultChange = { error ->
                emailError.value = error
            }
        )
    }
}

fun NavController.navigateToRecover(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.RecoverScreen.route, navOptions)

fun NavGraphBuilder.recoverScreen(
    modifier: Modifier = Modifier,
    onNavigateToVerify: (OTPType, String, String?) -> Unit
) {
    composable(
        route = NavigationRoute.RecoverScreen.route
    ){
        RecoverScreen(
            modifier = modifier,
            onNavigateToVerify = onNavigateToVerify
        )
    }
}
