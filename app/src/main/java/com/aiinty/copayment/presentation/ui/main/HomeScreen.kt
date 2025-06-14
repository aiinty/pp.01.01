package com.aiinty.copayment.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.presentation.navigation.NavigationRoute

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {

    }
}

fun NavController.navigateToHome(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.HomeScreen.route, navOptions)

fun NavGraphBuilder.homeScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.HomeScreen.route
    ){
        HomeScreen(
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        Modifier.fillMaxSize()
    )
}
