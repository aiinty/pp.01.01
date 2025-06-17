package com.aiinty.copayment.presentation.ui._components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.presentation.ui.theme.Green

@Composable
fun HomeHeader(
    color: Color = Green,
    leftItem: @Composable () -> Unit = {},
    rightItem: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.weight(1f)
        ) {
            leftItem()
        }
        Box(
            Modifier.size(48.dp)
        ) {
            rightItem()
        }
    }
}
