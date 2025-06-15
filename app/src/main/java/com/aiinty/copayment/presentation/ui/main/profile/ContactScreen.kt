package com.aiinty.copayment.presentation.ui.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.viewmodels.ProfileViewModel

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {

    }
}

fun NavGraphBuilder.contactScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.ContactScreen.route
    ){
        ContactScreen(
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactScreenPreview() {
    ContactScreen(
        Modifier.fillMaxSize()
    )
}