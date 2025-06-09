package com.aiinty.copayment.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.base.BaseIconButton

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        BaseIconButton(
            onClick = onNavigateToBack
        ) {
            Icon(
                painter = painterResource(R.drawable.chevron_left),
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}


fun NavController.navigateToHome(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.HomeScreen.route, navOptions)

fun NavGraphBuilder.homeScreen(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {}
) {
    composable(
        route = NavigationRoute.HomeScreen.route
    ){
        HomeScreen(
            modifier = modifier,
            onNavigateToBack = onNavigateToBack
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