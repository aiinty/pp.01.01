package com.aiinty.copayment.presentation.ui.main.home

import GroupedTransactionsList
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.card.BaseCardBot
import com.aiinty.copayment.presentation.ui._components.card.BaseCardTop
import com.aiinty.copayment.presentation.ui._components.home.HomeHeader
import com.aiinty.copayment.presentation.ui._components.home.OperationMenuRow
import com.aiinty.copayment.presentation.ui._components.home.TransactionTypeSheet
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale200
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.viewmodels.HomeUiState
import com.aiinty.copayment.presentation.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    UiErrorHandler(viewModel)

    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
    }
    val selectedCard = viewModel.selectedCard.collectAsState()

    when(val state = viewModel.uiState.value) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Error -> ErrorScreen(modifier)
        is HomeUiState.Success -> HomeScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            profile = state.profile,
            card = selectedCard.value!!,
            transactions = viewModel.transactions.collectAsState().value
        )
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    profile: Profile,
    card: Card,
    transactions: List<Transaction>
) {
    val headerColor = when(card.cardStyle) {
        CardStyle.MINIMAL -> Greyscale900
        else -> Green
    }
    val showTransactionTypeSheet = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        HomeHeader(
            headerColor,
            leftItem = {
                Column {
                    Text(
                        text = stringResource(R.string.welcome_back),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = profile.fullName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            rightItem = {
                BaseIconButton(
                    onClick = {

                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = headerColor,
                        contentColor = Color.White,
                        disabledContainerColor = headerColor,
                        disabledContentColor = Greyscale200
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.notification),
                        contentDescription = stringResource(R.string.notifications)
                    )
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .zIndex(1f)
        ) {
            BaseCardTop(Modifier.align(Alignment.BottomCenter), card)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .zIndex(0f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BaseCardBot(
                modifier = Modifier.shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ),
                card = card,
                showBalance = true
            )

            OperationMenuRow(
                onDepositClick = { viewModel.navigateToTransaction(TransactionType.DEPOSIT) },
                onTransferClick = { viewModel.navigateToTransfer() },
                onWithdrawClick = { viewModel.navigateToTransaction(TransactionType.WITHDRAW) },
                onMoreClick = { showTransactionTypeSheet.value = true }
            )

            if (showTransactionTypeSheet.value) {
                TransactionTypeSheet(
                    onSelect = { type ->
                        showTransactionTypeSheet.value = false
                        if (type != null) {
                            viewModel.navigateToTransaction(type)
                        }
                    },
                    onDismiss = { showTransactionTypeSheet.value = false }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.clickable {
                        viewModel.navigateToTransactionsHistory()
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.all_transactions),
                        color = Greyscale900,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.chevron_right),
                        contentDescription = stringResource(R.string.navigate),
                        tint = Greyscale900,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                GroupedTransactionsList(
                    cardTransactions = transactions,
                    card = card,
                    profile = profile,
                    canLoadMore = viewModel.canLoadMore.collectAsState().value,
                    onLoadMore = { viewModel.loadNextTransactions(card.id) }
                )
            }
        }
    }
}

fun NavGraphBuilder.homeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.HomeScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: HomeViewModel = hiltViewModel(parentEntry)

        HomeScreen(
            modifier = modifier,
            viewModel
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
