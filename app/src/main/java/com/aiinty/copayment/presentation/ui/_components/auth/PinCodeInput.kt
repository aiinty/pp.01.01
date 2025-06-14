package com.aiinty.copayment.presentation.ui._components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui.theme.Green

@Composable
fun PinCodeInput(
    modifier: Modifier = Modifier,
    pin: MutableState<String>,
    pinLength: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pinLength) { index ->
            val tokenLength = pin.value.length
            val isFilled = tokenLength - 1 >= index

            PinCell(isFilled)
        }
    }
}

@Composable
private fun PinCell(
    isFilled: Boolean
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    color = Green,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (isFilled) {
            Icon(
                painter = painterResource(R.drawable.ellipse),
                contentDescription = stringResource(R.string.pin_cell),
                tint = Color.Unspecified
            )
        }
    }
}
