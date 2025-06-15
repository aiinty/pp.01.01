package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiinty.copayment.presentation.navigation.BottomNavItem
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.ui.theme.Greyscale400
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus,
    items: List<BottomNavItem> = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cards,
        BottomNavItem.QRCode,
        BottomNavItem.Activity,
        BottomNavItem.Profile
    ),
    currentRoute: String?
) {
    val scope = rememberCoroutineScope()

    BottomAppBar(
        modifier = modifier
            .shadow(
                elevation = 24.dp,
                spotColor = Greyscale400
            ),
        containerColor = Color.White,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            when (item) {
                is BottomNavItem.QRCode -> {
                    BaseNavigationBarActionButton(
                        item = item,
                        onClick = {
                            if (!isSelected) {
                                scope.launch {
                                    navigationEventBus.send(NavigationEvent.ToRoute(item.route))
                                }
                            }
                        },
                    )
                }
                else -> {
                    BaseNavigationBarItem(
                        item = item,
                        onClick = {
                            if (!isSelected) {
                                scope.launch {
                                    navigationEventBus.send(NavigationEvent.ToRoute(item.route))
                                }
                            }
                        },
                        isSelected = isSelected
                    )
                }
            }
        }
    }
}
