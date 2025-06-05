package com.aiinty.copayment.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Greyscale900,
    secondary = Green,
    tertiary = Greyscale500
)

@Composable
fun CoPaymentTheme(
    content: @Composable () -> Unit
) {
    val colorScheme =  LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}