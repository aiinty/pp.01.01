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
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.common.CardNumberVisualTransformation
import com.aiinty.copayment.presentation.ui._components.base.BaseTextField
import com.aiinty.copayment.presentation.ui.theme.Typography


@Composable
fun CardNumberTextField(
    modifier: Modifier = Modifier,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    labelResId: Int? = null,
    validateRules: (String) -> Int? = { number ->
        when {
            number.length < 16 -> R.string.error_card_number_length
            !number.all { it.isDigit() } -> R.string.error_card_number_format
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
        value = cardNumber,
        onValueChange = {
            onCardNumberChange(it.filter { ch -> ch.isDigit() }.take(16))
            validateInput(it)
        },
        label = {
            Text(
                text = if (labelResId != null) stringResource(labelResId)
                else stringResource(R.string.card_number)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = errorMessage.value != null,
        visualTransformation = CardNumberVisualTransformation()
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
