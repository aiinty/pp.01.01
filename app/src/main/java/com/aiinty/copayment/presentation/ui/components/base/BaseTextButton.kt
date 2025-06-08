package com.aiinty.copayment.presentation.ui.components.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.presentation.ui.theme.Greyscale400
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun BaseTextButton(
    text: String,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    enabledColor: Color = Color.Black,
    disabledColor: Color = Greyscale400,
    style: TextStyle = Typography.titleMedium,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = contentModifier,
            text = text,
            style = style,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            color = if (enabled) enabledColor else disabledColor,
        )
    }
}