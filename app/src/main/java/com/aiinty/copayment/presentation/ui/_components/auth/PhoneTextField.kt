package com.aiinty.copayment.presentation.ui._components.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui._components.base.BaseTextField
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun PhoneTextField(
    modifier: Modifier = Modifier,
    phone: String,
    onPhoneChange: (String) -> Unit,
    labelResId: Int? = null,
    validateRules: (String) -> Int? = { input ->
        when {
            input.isEmpty() -> R.string.error_empty_phone
            !input.matches(Regex("^\\+?[0-9]{7,15}\$")) -> R.string.error_invalid_phone
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
        value = phone,
        onValueChange = {
            onPhoneChange(it)
            validateInput(it)
        },
        label = {
            Text(
                text = if (labelResId != null) stringResource(labelResId)
                else stringResource(R.string.phone_number)
            )
        },
        isError = errorMessage.value != null,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone
        )
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
