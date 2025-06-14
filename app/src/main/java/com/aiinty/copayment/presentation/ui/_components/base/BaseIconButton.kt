package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.presentation.ui.theme.Greyscale200

@Composable
fun BaseIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .size(40.dp),
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(
        containerColor = Color.White,
        contentColor = Color.Black,
        disabledContainerColor = Color.White,
        disabledContentColor = Greyscale200
    ),
    border: BorderStroke? = BorderStroke(1.dp, Greyscale200),
    content: @Composable () -> Unit
) {
    OutlinedIconButton (
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
