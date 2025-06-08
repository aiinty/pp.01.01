package com.aiinty.copayment.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.auth.PasswordTextField
import com.aiinty.copayment.presentation.ui.components.base.BaseButton
import com.aiinty.copayment.presentation.ui.components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun PasswordChangeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { target ->
            when(target) {
                is NavigationRoute.SignInScreen -> {
                    onNavigateToSignIn()
                }
                else -> {}
            }
        }
    }

    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf<String?>(null) }
    val passwordConfirm = remember { mutableStateOf("") }
    val passwordConfirmError = remember { mutableStateOf<String?>(null) }
    val isInputsValidated = passwordError.value == null && passwordConfirmError.value == null &&
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

            PasswordChangeHeader()

            PasswordChangeFields(
                password = password,
                passwordError = passwordError,
                passwordConfirm = passwordConfirm,
                passwordConfirmError = passwordConfirmError,
            )
        }

        BaseButton(
            onClick = {
                if (isInputsValidated) {
                    viewModel.updateUser(null, password.value)
                }
            },
            enabled = isInputsValidated
        ) {
            Text(
                text = stringResource(R.string.create_new_password),
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PasswordChangeHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.password_change_title),
            style = Typography.titleMedium
        )
        Text(
            text = stringResource(R.string.password_change_desc),
            style = Typography.bodyMedium
        )
    }
}

@Composable
private fun PasswordChangeFields(
    modifier: Modifier = Modifier,
    password: MutableState<String>,
    passwordError: MutableState<String?>,
    passwordConfirm: MutableState<String>,
    passwordConfirmError: MutableState<String?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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


fun NavController.navigateToPasswordChange(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.PasswordChangeScreen.route, navOptions)

fun NavGraphBuilder.passwordChangeScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    composable(
        route = NavigationRoute.PasswordChangeScreen.route
    ){
        PasswordChangeScreen(
            modifier = modifier,
            onNavigateToBack = onNavigateToBack,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}
