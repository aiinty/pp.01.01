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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import com.aiinty.copayment.presentation.ui._components.auth.FullNameTextField
import com.aiinty.copayment.presentation.ui._components.auth.PasswordTextField
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    UiErrorHandler(viewModel = viewModel)

    val fullName = remember { mutableStateOf("") }
    val fullNameError = remember { mutableStateOf<Int?>(null) }
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf<Int?>(null) }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf<Int?>(null) }
    val passwordConfirm = remember { mutableStateOf("") }
    val passwordConfirmError = remember { mutableStateOf<Int?>(null) }
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
                onClick = { viewModel.navigateToSignIn() }
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
    fullNameError: MutableState<Int?>,
    email: MutableState<String>,
    emailError: MutableState<Int?>,
    password: MutableState<String>,
    passwordError: MutableState<Int?>,
    passwordConfirm: MutableState<String>,
    passwordConfirmError: MutableState<Int?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FullNameTextField(
            modifier = Modifier.fillMaxWidth(),
            fullName = fullName.value,
            onFullNameChange = { fullName.value=it },
            onValidationResultChange={ error ->
                fullNameError.value=error
            }
        )

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

        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            password = passwordConfirm.value,
            onPasswordChange = { passwordConfirm.value = it },
            labelResId = R.string.confirm_password,
            validateRules = { pass ->
                if (pass != password.value) R.string.error_match_passwords
                else null
            },
            onValidationResultChange = { error ->
                passwordConfirmError.value = error
            }
        )
    }
}

fun NavGraphBuilder.signUpScreen(
    modifier: Modifier = Modifier
) {
    composable(
        route = NavigationRoute.SignUpScreen.route
    ){
        SignUpScreen(
            modifier = modifier,
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
