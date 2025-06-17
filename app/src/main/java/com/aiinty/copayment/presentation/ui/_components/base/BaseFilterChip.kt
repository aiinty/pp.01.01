package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@Composable
fun BaseFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(
        containerColor = Color.White,
        labelColor = Greyscale900,
        selectedContainerColor = Greyscale900,
        selectedLabelColor = Color.White
    ),
    border: BorderStroke? = if (selected) BorderStroke(1.dp, Greyscale900) else null
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        colors = colors,
        border = border
    )
}
