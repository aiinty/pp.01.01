package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.model.KeyboardInputType
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@Composable
fun NumericKeyboard(
    modifier: Modifier = Modifier,
    input: MutableState<String>,
    validateRules: (String) -> Boolean = { true },
    keyboardInputType: KeyboardInputType = KeyboardInputType.DECIMAL
) {
    val keys = when(keyboardInputType) {
        KeyboardInputType.ONLY_NUMBERS -> {
            listOf(
                listOf("1", "4", "7", " "),
                listOf("2", "5", "8", "0"),
                listOf("3", "6", "9", "<")
            )
        }
        KeyboardInputType.DECIMAL -> {
            listOf(
                listOf("1", "4", "7", "."),
                listOf("2", "5", "8", "0"),
                listOf("3", "6", "9", "<")
            )
        }
    }

    fun handleKeyPress(key: String) {
        when (key) {
            "<" -> {
                if (input.value.isNotEmpty()) {
                    input.value = input.value.dropLast(1)
                }
            }
            else -> {
                if (validateRules(input.value)) {
                    input.value += key
                }
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            keys.forEach { columnKeys ->
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    columnKeys.forEach { key ->
                        if (key.isNotEmpty()) {
                            KeyboardButton(
                                key = key,
                                modifier = Modifier
                                    .height(56.dp)
                                    .fillMaxWidth(),
                                onClick = { handleKeyPress(key) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyboardButton(
    key: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    when (key) {
        "<" -> {
            BaseIconButton(
                modifier = modifier,
                border = null,
                onClick = onClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.backspace),
                    contentDescription = stringResource(R.string.backspace)
                )
            }
        }
        " " -> {
            Text(
                text = key,
                modifier = modifier,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
        else -> {
            BaseTextButton(
                text = key,
                modifier = modifier.clip(RoundedCornerShape(12.dp)),
                contentModifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                enabledColor = Greyscale900,
                onClick = onClick,
            )
        }
    }
}
