package com.aiinty.copayment.presentation.ui.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale50

@Composable
fun OTPInput(
    modifier: Modifier = Modifier,
    token: MutableState<String>,
    tokenLength: Int = 6
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(tokenLength) { index ->
            val currentLength = token.value.length
            val isFilled = currentLength - 1 >= index
            val borderColor = when {
                index == currentLength - 1 -> Green
                else -> Color.Transparent
            }
            TokenCell(
                borderColor = borderColor,
                input = if (isFilled) token.value[index].toString() else ""
            )
        }
    }
}

@Composable
private fun TokenCell(
    borderColor: Color,
    input: String
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Greyscale50)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = input
        )
    }
}
