package com.aiinty.copayment.presentation.ui.components.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui.components.base.BaseTextField
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    label: String = "Password",
    errorPasswordLength: String = "Password must be at least 6 characters",
    validateRules: (String) -> String? = { pass ->
        if (pass.length < 6) errorPasswordLength else null
    },
    onValidationResultChange: ((String?) -> Unit)? = null,
) {
    val isPasswordVisible = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    fun validateInput(input: String) {
        val error = validateRules(input)
        errorMessage.value = error
        onValidationResultChange?.invoke(error)
    }

    BaseTextField(
        modifier = modifier,
        value = password,
        onValueChange = {
            onPasswordChange(it)
            validateInput(it)
        },
        label = { Text(text = label) },
        visualTransformation = if (isPasswordVisible.value)
            VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible.value = !isPasswordVisible.value }
            ) {
                Icon(
                    painter = if (isPasswordVisible.value)
                        painterResource(R.drawable.eye) else painterResource(R.drawable.eye_off),
                    contentDescription = if (isPasswordVisible.value)
                        stringResource(R.string.hide) else stringResource(R.string.show)
                )
            }
        },
        isError = errorMessage.value != null
    )

    errorMessage.value?.let { error ->
        Text(
            text = error,
            color = Color.Red,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
