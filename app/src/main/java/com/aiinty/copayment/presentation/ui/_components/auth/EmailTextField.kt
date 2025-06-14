package com.aiinty.copayment.presentation.ui._components.auth

import android.util.Patterns
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui._components.base.BaseTextField
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    labelResId: Int? = null,
    validateRules: (String) -> Int? = { emailInput ->
        when {
            emailInput.isEmpty() -> R.string.error_empty_email
            !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> R.string.error_invalid_email
            else -> null
        }
    },
    onValidationResultChange: ((Int?) -> Unit)? = null,
) {
    val errorMessage = remember { mutableStateOf<Int?>(null) }

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
        label = {
            Text(
                text = if (labelResId != null) stringResource(labelResId) else
                    stringResource(R.string.email))
        },
        isError = errorMessage.value != null
    )

    errorMessage.value?.let { error ->
        Text(
            text = stringResource(error),
            color = Color.Red,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
