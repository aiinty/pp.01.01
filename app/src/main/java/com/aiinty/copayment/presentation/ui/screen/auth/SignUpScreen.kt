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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import com.aiinty.copayment.presentation.ui.components.auth.FullNameTextField
import com.aiinty.copayment.presentation.ui.components.auth.PasswordTextField
import com.aiinty.copayment.presentation.ui.components.base.BaseButton
import com.aiinty.copayment.presentation.ui.components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui.components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToVerify: (OTPType, String, String?) -> Unit = { _, _, _ -> },
) {
    CollectNavigationEvents(
        navigationFlow = viewModel.navigationEvent,
        onNavigateToVerify = { type, email, nextDestination ->
            onNavigateToVerify(type, email, nextDestination)
        }
    )
    AuthErrorHandler(viewModel = viewModel)

    val fullName = remember { mutableStateOf("") }
    val fullNameError = remember { mutableStateOf<String?>(null) }
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf<String?>(null) }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf<String?>(null) }
    val passwordConfirm = remember { mutableStateOf("") }
    val passwordConfirmError = remember { mutableStateOf<String?>(null) }
    val isInputsValidated = fullNameError.value == null && emailError.value == null &&
            passwordError.value == null && passwordConfirmError.value == null &&
            fullName.value.isNotEmpty() && email.value.isNotEmpty() &&
            password.value.isNotEmpty() && passwordConfirm.value.isNotEmpty()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BaseIconButton(
                onClick = onNavigateToBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left),
                    contentDescription = stringResource(R.string.back)
                )
            }

            SignUpHeader()

            SignUpFields(
                fullName = fullName,
                fullNameError = fullNameError,
                email = email,
                emailError = emailError,
                password = password,
                passwordError = passwordError,
                passwordConfirm = passwordConfirm,
                passwordConfirmError = passwordConfirmError,
            )

            BaseButton(
                onClick = {
                    if (isInputsValidated) {
                        viewModel.signUp(fullName.value, email.value, password.value)
                    }
                },
                enabled = isInputsValidated
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
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
                text = stringResource(R.string.sign_up_already_have),
                style = Typography.bodyMedium
            )

            BaseTextButton(
                modifier = Modifier.padding(horizontal = 2.dp),
                text = stringResource(R.string.sign_in),
                enabledColor = Green,
                onClick = onNavigateToSignIn
            )
        }
    }
}

@Composable
private fun SignUpHeader(
    modifier: Modifier = Modifier
)  {
    val title = stringResource(R.string.sign_up_title, stringResource(R.string.app_name))

    val annotatedTitle = buildAnnotatedString {
        append(title)
        val startIndex = title.indexOf(stringResource(R.string.app_name))
        if (startIndex >= 0) {
            val endIndex = startIndex + stringResource(R.string.app_name).length
            addStyle(
                style = SpanStyle(color = Green),
                start = startIndex,
                end = endIndex
            )
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = annotatedTitle,
            style = Typography.titleMedium
        )
    }
}

@Composable
private fun SignUpFields(
    modifier: Modifier = Modifier,
    fullName: MutableState<String>,
    fullNameError: MutableState<String?>,
    email: MutableState<String>,
    emailError: MutableState<String?>,
    password: MutableState<String>,
    passwordError: MutableState<String?>,
    passwordConfirm: MutableState<String>,
    passwordConfirmError: MutableState<String?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val errorEmptyFullName = stringResource(R.string.full_name_cannot_be_empty)
        val errorInvalidFullName = stringResource(R.string.only_letters_allowed)
        FullNameTextField(
            modifier = Modifier.fillMaxWidth(),
            fullName = fullName.value,
            errorEmptyFullName = errorEmptyFullName,
            errorInvalidFullName = errorInvalidFullName,
            onFullNameChange = { fullName.value=it },
            onValidationResultChange={ error ->
                fullNameError.value=error
            }
        )

        val errorEmptyEmail = stringResource(R.string.email_cannot_be_empty)
        val errorInvalidEmail = stringResource(R.string.invalid_email_address)
        EmailTextField(
            modifier = Modifier.fillMaxWidth(),
            email = email.value,
            onEmailChange = { email.value=it },
            errorEmptyEmail = errorEmptyEmail,
            errorInvalidEmail = errorInvalidEmail,
            onValidationResultChange = { error ->
                emailError.value=error
            }
        )

        val errorPasswordLength = stringResource(R.string.password_must_be)
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            password = password.value,
            errorPasswordLength = errorPasswordLength,
            onPasswordChange = { password.value = it },
            onValidationResultChange = { error ->
                passwordError.value = error
            }
        )

        val errorPasswordMustMuch = stringResource(R.string.passwords_must_match)
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            password = passwordConfirm.value,
            onPasswordChange = { passwordConfirm.value = it },
            label = stringResource(R.string.confirm_password),
            validateRules = { pass ->
                if (pass != password.value) errorPasswordMustMuch else null
            },
            onValidationResultChange = { error ->
                passwordConfirmError.value = error
            }
        )
    }
}


fun NavController.navigateToSignUp(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.SignUpScreen.route, navOptions)

fun NavGraphBuilder.signUpScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToVerify: (OTPType, String, String?) -> Unit,
) {
    composable(
        route = NavigationRoute.SignUpScreen.route
    ){
        SignUpScreen(
            modifier = modifier,
            onNavigateToBack = onNavigateToBack,
            onNavigateToSignIn = onNavigateToSignIn,
            onNavigateToVerify = onNavigateToVerify,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        Modifier.fillMaxSize()
    )
}
