package com.aiinty.copayment.presentation.ui.components.base

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.aiinty.copayment.presentation.navigation.BottomNavItem
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@Composable
fun RowScope.BaseNavigationBarItem(
    item: BottomNavItem,
    onClick: () -> Unit = {},
    isSelected: Boolean = false
) {

    NavigationBarItem(
        icon = {
            Icon(
                painter = if (item.iconResId != null) painterResource(item.iconResId) else
                    rememberVectorPainter(Icons.Default.Menu),
                contentDescription = item.label
            )
        },
        label = {
            Text(
                text = item.label,
                color = if (isSelected) Greyscale900 else Greyscale500,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        selected = isSelected,
        onClick = {
            onClick()
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Greyscale900,
            unselectedIconColor = Greyscale500,
            indicatorColor = Color.Transparent
        )
    )
}