package com.aiinty.copayment.presentation.ui._components.auth

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
fun FullNameTextField(
    modifier: Modifier = Modifier,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    labelResId: Int? = null,
    validateRules: (String) -> Int? = { name ->
        val pattern = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$".toRegex()
        when {
            name.isBlank() -> R.string.error_empty_full_name
            !pattern.matches(name) -> R.string.error_invalid_full_name
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
        value = fullName,
        onValueChange = {
            val filtered = it.filter { char ->
                char.isLetter() || char.isWhitespace() || char == '-'
            }
            onFullNameChange(filtered)
            validateInput(filtered)
        },
        label = {
            Text(
                text = if (labelResId != null) stringResource(labelResId) else
                    stringResource(R.string.full_name)
            )
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
