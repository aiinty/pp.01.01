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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.CollectNavigationEvents
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.auth.PasswordTextField
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun PasswordChangeScreen(
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

    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf<Int?>(null) }
    val passwordConfirm = remember { mutableStateOf("") }
    val passwordConfirmError = remember { mutableStateOf<Int?>(null) }
    val isInputsValidated = passwordError.value == null && passwordConfirmError.value == null &&
            password.value.isNotEmpty() && passwordConfirm.value.isNotEmpty()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

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
    passwordError: MutableState<Int?>,
    passwordConfirm: MutableState<String>,
    passwordConfirmError: MutableState<Int?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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


fun NavController.navigateToPasswordChange(
    nextDestination: String? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(NavigationRoute.PasswordChangeScreen(nextDestination).route, navOptions)
}

fun NavGraphBuilder.passwordChangeScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onNavigateToNext: (String) -> Unit
) {
    composable(
        route = "${NavigationRoute.PasswordChangeScreen().route}?next={next}",
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
            PasswordChangeScreen(
                modifier = modifier,
                nextDestination = nextDestination,
                onNavigateToNext = onNavigateToNext
            )
        } else {
            onNavigateToBack()
        }
    }
}
