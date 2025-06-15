package com.aiinty.copayment.presentation.ui._components.card

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui._components.base.BaseTextField
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun CardCvvTextField(
    modifier: Modifier = Modifier,
    cvv: String,
    onCvvChange: (String) -> Unit,
    labelResId: Int? = null,
    validateRules: (String) -> Int? = { cvvCode ->
        when {
            cvvCode.length < 3 -> R.string.error_cvv_length
            !cvvCode.all { it.isDigit() } -> R.string.error_cvv_digits
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
        value = cvv,
        onValueChange = {
            onCvvChange(it.filter { ch -> ch.isDigit() }.take(4))
            validateInput(it)
        },
        label = {
            Text(
                text = if (labelResId != null) stringResource(labelResId)
                else stringResource(R.string.cvv)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        isError = errorMessage.value != null
    )

    errorMessage.value?.let { error ->
        if (showError){
            Text(
                text = stringResource(error),
                color = Color.Red,
                style = Typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
