package com.aiinty.copayment.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun TopShadow(
    modifier: Modifier = Modifier,
    shadowHeight: Float = 40f,
    shadowColor: Color = Color.Black,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.drawBehind {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        shadowColor,
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = -shadowHeight
                ),
                size = size.copy(height = shadowHeight),
                topLeft = Offset(0f, -shadowHeight)
            )
        }
    ) {
        content()
    }
}