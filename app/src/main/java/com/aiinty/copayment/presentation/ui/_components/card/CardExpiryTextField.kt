package com.aiinty.copayment.presentation.ui._components.card

import CardExpiryDateVisualTransformation
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
fun CardExpiryTextField(
    modifier: Modifier = Modifier,
    expiry: String,
    onExpiryChange: (String) -> Unit,
    labelResId: Int? = null,
    validateRules: (String) -> Int? = { date ->
        val regex = Regex("^(0[1-9]|1[0-2])\\/\\d{2}$")
        when {
            !regex.matches(date) -> R.string.error_expiry_format
            else -> null
        }
    },
    onValidationResultChange: ((Int?) -> Unit)? = null,
    showError: Boolean = true,
) {
    val errorMessage = remember { mutableStateOf<Int?>(null) }

    fun validateInput(input: String) {
        val error = validateRules(input)
        errorMessage.value = error
        onValidationResultChange?.invoke(error)
    }

    BaseTextField(
        modifier = modifier,
        value = expiry,
        onValueChange = { value ->
            val digits = value.filter { it.isDigit() }.take(4)
            onExpiryChange(digits)

            val formatted = if (digits.length <= 2) digits else digits.substring(0, 2) + "/" + digits.substring(2)
            validateInput(formatted)
        },
        label = {
            Text(
                text = if (labelResId != null) stringResource(labelResId)
                else stringResource(R.string.expiry_date)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = errorMessage.value != null,
        visualTransformation = CardExpiryDateVisualTransformation()
    )

    errorMessage.value?.let { error ->
        if (showError) {
            Text(
                text = stringResource(error),
                color = Color.Red,
                style = Typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
