package com.aiinty.copayment.presentation.ui.screen.main

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
fun QRCodeScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {

    }
}


fun NavController.navigateToQRCode(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.QRCodeScreen.route, navOptions)

fun NavGraphBuilder.qrCodeScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.QRCodeScreen.route
    ){
        QRCodeScreen(
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    QRCodeScreen(
        Modifier.fillMaxSize()
    )
}