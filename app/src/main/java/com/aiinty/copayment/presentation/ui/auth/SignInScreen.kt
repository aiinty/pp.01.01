package com.aiinty.copayment.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.auth.EmailTextField
import com.aiinty.copayment.presentation.ui._components.auth.PasswordTextField
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    UiErrorHandler(viewModel = viewModel)

    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf<Int?>(null) }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf<Int?>(null) }
    val isInputsValidated = emailError.value == null && passwordError.value == null &&
            email.value.isNotEmpty() && password.value.isNotEmpty()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SignInHeader()

            SignInFields(
                email = email,
                emailError = emailError,
                password = password,
                passwordError = passwordError
            )

            BaseTextButton(
                text = stringResource(R.string.sign_in_forgot_password),
                enabledColor = Green,
                onClick = { viewModel.navigateToForgotPassword() }
            )

            BaseButton(
                onClick = {
                    if (isInputsValidated) {
                        viewModel.signIn(email.value, password.value)
                    }
                },
                enabled = isInputsValidated
            ) {
                Text(
                    text = stringResource(R.string.sign_in),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.sign_in_dont_have),
                style = Typography.bodyMedium
            )

            BaseTextButton(
                modifier = Modifier.padding(horizontal = 2.dp),
                text = stringResource(R.string.sign_up),
                enabledColor = Green,
                onClick = { viewModel.navigateToSignUp() }
            )
        }
    }
}

@Composable
private fun SignInHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.sign_in_title),
            style = Typography.titleMedium
        )
        Text(
            text = stringResource(R.string.sign_in_desc),
            style = Typography.bodyMedium
        )
    }
}

@Composable
private fun SignInFields(
    modifier: Modifier = Modifier,
    email: MutableState<String>,
    emailError: MutableState<Int?>,
    password: MutableState<String>,
    passwordError: MutableState<Int?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EmailTextField(
            modifier = Modifier.fillMaxWidth(),
            email = email.value,
            onEmailChange = { email.value=it },
            onValidationResultChange = { error ->
                emailError.value=error
            }
        )

        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            password = password.value,
            onPasswordChange = { password.value = it },
            onValidationResultChange = { error ->
                passwordError.value = error
            }
        )
    }
}


fun NavGraphBuilder.signInScreen(
    modifier: Modifier = Modifier,
    ) {
    composable(
        route = NavigationRoute.SignInScreen.route
    ){
        SignInScreen(
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        Modifier.fillMaxSize()
    )
}