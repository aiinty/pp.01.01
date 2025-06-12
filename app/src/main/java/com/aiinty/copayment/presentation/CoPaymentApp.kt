package com.aiinty.copayment.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiinty.copayment.presentation.navigation.CoPaymentNavHost
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.base.BottomNavigationBar

@Composable
fun CoPaymentApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val navBackStackEntry  = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    val currentNavigationRoute = remember(currentDestination?.route) {
        NavigationRoute.routes[currentDestination?.route]
    }
    val showBottomBar = currentNavigationRoute?.showBottomBar ?: false

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        CoPaymentNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CoPaymentApp(
        modifier = Modifier.fillMaxSize()
    )
}
