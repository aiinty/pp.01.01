package com.aiinty.copayment.presentation.ui.components.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@Composable
fun BaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().height(53.dp),
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Greyscale900,
        contentColor = Color.White,
        disabledContainerColor = Greyscale500,
        disabledContentColor = Color.White
    ),
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border
    ) {
        content()
    }
}