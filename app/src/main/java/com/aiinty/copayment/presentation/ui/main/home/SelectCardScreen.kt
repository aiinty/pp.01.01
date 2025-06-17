package com.aiinty.copayment.presentation.ui.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.card.CardPicker
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.viewmodels.HomeUiState
import com.aiinty.copayment.presentation.viewmodels.HomeViewModel

@Composable
fun SelectCardScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    UiErrorHandler(viewModel)

    when(val state = viewModel.uiState.value) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Error -> ErrorScreen(modifier)
        is HomeUiState.Success -> SelectCardScreenContent(
            modifier,
            state.cards,
            viewModel
        )
    }
}

@Composable
private fun SelectCardScreenContent(
    modifier: Modifier = Modifier,
    cards: List<Card>,
    viewModel: HomeViewModel,
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardPicker(
            cards = cards,
            onSelect = { card ->
                viewModel.selectCard(card)
            }
        )
    }
}

fun NavGraphBuilder.selectCardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.SelectCardScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: HomeViewModel = hiltViewModel(parentEntry)

        SelectCardScreen(
            modifier = modifier,
            viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectCardScreenPreview() {
    SelectCardScreen(
        Modifier.fillMaxSize(),
    )
}
