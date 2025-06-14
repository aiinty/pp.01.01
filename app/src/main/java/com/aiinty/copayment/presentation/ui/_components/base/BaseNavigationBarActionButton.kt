package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.BottomNavItem
import com.aiinty.copayment.presentation.ui.theme.Green

@Composable
fun RowScope.BaseNavigationBarActionButton(
    item: BottomNavItem,
    onClick: () -> Unit = {}
) {
    val itemLabel = if (item.labelResId != null) stringResource(item.labelResId) else
        stringResource(R.string.bar_item)

    Box(
        modifier = Modifier.Companion
            .weight(1f)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = Green.copy(alpha = 0.4f)
                )
                .clip(CircleShape)
                .background(Green)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = if (item.iconResId != null) painterResource(item.iconResId) else
                    rememberVectorPainter(Icons.Default.Menu),
                contentDescription = itemLabel,
                tint = Color.White
            )
        }
    }
}
