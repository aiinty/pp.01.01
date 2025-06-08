package com.aiinty.copayment.presentation.ui.components.auth

import android.util.Patterns
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.presentation.ui.components.base.BaseTextField
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    label: String = "Email",
    errorEmptyEmail: String = "Email cannot be empty",
    errorInvalidEmail: String = "Invalid email address",
    validateRules: (String) -> String? = { emailInput ->
        when {
            emailInput.isEmpty() -> errorEmptyEmail
            !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> errorInvalidEmail
            else -> null
        }
    },
    onValidationResultChange: ((String?) -> Unit)? = null,
) {
    val errorMessage = remember { mutableStateOf<String?>(null) }

    fun validateInput(input: String) {
        val error = validateRules(input)
        errorMessage.value = error
        onValidationResultChange?.invoke(error)
    }

    BaseTextField(
        modifier = modifier,
        value = email,
        onValueChange = {
            onEmailChange(it)
            validateInput(it)
        },
        label = { Text(text = label) },
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
