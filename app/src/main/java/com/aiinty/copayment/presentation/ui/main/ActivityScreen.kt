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
fun ActivityScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {

    }
}

fun NavController.navigateToActivity(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.ActivityScreen.route, navOptions)

fun NavGraphBuilder.activityScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.ActivityScreen.route
    ){
        ActivityScreen(
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ActivityScreen(
        Modifier.fillMaxSize()
    )
}
