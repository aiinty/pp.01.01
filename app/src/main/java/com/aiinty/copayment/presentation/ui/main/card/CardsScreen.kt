package com.aiinty.copayment.presentation.ui.main.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.BasePullToRefreshBox
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.card.BaseCard
import com.aiinty.copayment.presentation.ui._components.card.CardPicker
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.viewmodels.CardUiState
import com.aiinty.copayment.presentation.viewmodels.CardViewModel

@Composable
fun CardsScreen(
    modifier: Modifier = Modifier,
    viewModel: CardViewModel = hiltViewModel(),
) {
    UiErrorHandler(viewModel)
    LaunchedEffect(Unit) {
        viewModel.loadCards()
    }
    when(val state = viewModel.uiState.value) {
        is CardUiState.Loading -> LoadingScreen(modifier)
        is CardUiState.Error -> ErrorScreen(modifier)
        is CardUiState.Success -> CardsScreenContent(modifier, state.cards, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardsScreenContent(
    modifier: Modifier,
    cards: List<Card>,
    viewModel: CardViewModel
) {
    val isRefreshing = remember { mutableStateOf(false) }

    BasePullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = { viewModel.loadCards() }
    ) {
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardPicker(
                cards = cards,
                onSelect = { card ->
                    viewModel.selectCard(card)
                    viewModel.navigateToEditCard()
                }
            )

            item {
                BaseButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = {
                        viewModel.navigateToCreateStyleCard()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Greyscale50,
                        contentColor = Greyscale900,
                        disabledContainerColor = Greyscale50,
                        disabledContentColor = Greyscale900
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = stringResource(R.string.plus)
                        )

                        Text(
                            text = stringResource(R.string.add_new_card),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                            color = Greyscale900
                        )
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.cardsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    composable(
        route = NavigationRoute.CardsScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)
        CardsScreen(
            modifier = modifier,
            viewModel = viewModel
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
