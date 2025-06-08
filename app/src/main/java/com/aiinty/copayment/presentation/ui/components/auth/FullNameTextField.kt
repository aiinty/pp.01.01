package com.aiinty.copayment.presentation.ui.components.auth

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
fun FullNameTextField(
    modifier: Modifier = Modifier,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    label: String = "Full Name",
    errorEmptyFullName: String = "Full Name cannot be empty",
    errorInvalidFullName: String = "Only letters, spaces, and hyphens are allowed",
    validateRules: (String) -> String? = { name ->
        val pattern = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$".toRegex()
        when {
            name.isEmpty() -> errorEmptyFullName
            !pattern.matches(name) -> errorInvalidFullName
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
        value = fullName,
        onValueChange = {
            val filtered = it.filter { char ->
                char.isLetter() || char.isWhitespace() || char == '-'
            }
            onFullNameChange(filtered)
            validateInput(filtered)
        },
        label = { Text(text = label) },
        isError = errorMessage.value != null
    )

    errorMessage.value?.let { error ->
        Text(
            text = error,
            color = Color.Red,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(start=16.dp)
        )
    }
}
