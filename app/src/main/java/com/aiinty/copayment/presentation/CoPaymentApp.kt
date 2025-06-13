package com.aiinty.copayment.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiinty.copayment.presentation.navigation.CoPaymentNavHost
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.TopBarState
import com.aiinty.copayment.presentation.navigation.rememberTopBarState
import com.aiinty.copayment.presentation.ui.components.base.BaseTopBar
import com.aiinty.copayment.presentation.ui.components.base.BottomNavigationBar

@Composable
fun CoPaymentApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val navBackStackEntry  = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    val currentNavigationRoute = remember(currentDestination) {
        NavigationRoute.findByRoute(currentDestination?.route)
    }
    val topBarState = rememberTopBarState(currentNavigationRoute, navController)
    val showBottomBar = currentNavigationRoute?.showBottomBar ?: false

    val targetTopPadding = if (topBarState.isVisible) 80.dp else 0.dp
    val animatedTopPadding = animateDpAsState(
        targetValue = targetTopPadding,
        label = "top-padding"
    )

    val targetBottomPadding = if (showBottomBar) 80.dp else 0.dp
    val animatedBottomPadding = animateDpAsState(
        targetValue = targetBottomPadding,
        label = "bottom-padding"
    )

    Scaffold(
        modifier = modifier,
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .height(80.dp),
                visible = topBarState.isVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
            ) {
                BaseTopBar(
                    state = topBarState
                )
            }

            CoPaymentNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        top = animatedTopPadding.value,
                        bottom = animatedBottomPadding.value
                    ),
                navController = navController
            )

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .height(80.dp),
                visible = showBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavigationBar(
                    navController = navController
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CoPaymentApp(
        modifier = Modifier.fillMaxSize()
    )
}
