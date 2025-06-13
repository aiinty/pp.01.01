package com.aiinty.copayment.presentation.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.presentation.navigation.CollectNavigationEvents
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.auth.AuthErrorHandler
import com.aiinty.copayment.presentation.ui.components.auth.EmailTextField
import com.aiinty.copayment.presentation.ui.components.auth.PasswordTextField
import com.aiinty.copayment.presentation.ui.components.base.BaseButton
import com.aiinty.copayment.presentation.ui.components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui.components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToVerify: (OTPType, String, String?) -> Unit = { _, _, _ -> },
    onNavigateToPinCode: () -> Unit = {},
    onNavigateToCreatePinCode: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToVerify = { type, email, nextDestination ->
            onNavigateToVerify(type, email, nextDestination)
        },
        onNavigateToPinCode = onNavigateToPinCode,
        onNavigateToCreatePinCode = onNavigateToCreatePinCode,
        onNavigateToHome = onNavigateToHome
    )
    AuthErrorHandler(viewModel = viewModel)

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
                onClick = onNavigateToForgotPassword
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
                onClick = onNavigateToSignUp
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


fun NavController.navigateToSignIn(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.SignInScreen.route, navOptions)

fun NavGraphBuilder.signInScreen(
    modifier: Modifier = Modifier,
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToVerify: (OTPType, String, String?) -> Unit,
    onNavigateToPinCode: () -> Unit = {},
    onNavigateToCreatePinCode: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    ) {
    composable(
        route = NavigationRoute.SignInScreen.route
    ){
        SignInScreen(
            modifier = modifier,
            onNavigateToForgotPassword = onNavigateToForgotPassword,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToVerify = onNavigateToVerify,
            onNavigateToPinCode = onNavigateToPinCode,
            onNavigateToCreatePinCode = onNavigateToCreatePinCode,
            onNavigateToHome = onNavigateToHome
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