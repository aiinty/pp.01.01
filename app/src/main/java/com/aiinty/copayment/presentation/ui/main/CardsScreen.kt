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
fun CardsScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {

    }
}

fun NavController.navigateToCards(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.CardsScreen.route, navOptions)

fun NavGraphBuilder.cardsScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.CardsScreen.route
    ){
        CardsScreen(
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CardsScreen(
        Modifier.fillMaxSize()
    )
}
