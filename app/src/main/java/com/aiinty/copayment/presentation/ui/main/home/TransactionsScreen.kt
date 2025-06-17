package com.aiinty.copayment.presentation.ui.main.home

import GroupedTransactionsList
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionCategory
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.home.HomeHeader
import com.aiinty.copayment.presentation.ui._components.home.TransactionsFilterRow
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale200
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.utils.CardUtils
import com.aiinty.copayment.presentation.viewmodels.HomeUiState
import com.aiinty.copayment.presentation.viewmodels.HomeViewModel

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    UiErrorHandler(viewModel)
    val selectedCard = viewModel.selectedCard.collectAsState()

    when(val state = viewModel.uiState.value) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Error -> ErrorScreen(modifier)
        is HomeUiState.Success -> TransactionsScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            profile = state.profile,
            card = selectedCard.value!!,
            transactions = state.transactions,
        )
    }
}

@Composable
private fun TransactionsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    profile: Profile,
    card: Card,
    transactions: List<Transaction>,
) {
    val headerColor = when (card.cardStyle) {
        CardStyle.MINIMAL -> Greyscale900
        else -> Green
    }
    val isNumberVisible = remember { mutableStateOf(false) }

    var selectedCategories = remember { mutableStateOf<Set<TransactionCategory>>(emptySet()) }
    var selectedTypes = remember { mutableStateOf<Set<TransactionType>>(emptySet()) }
    val filteredTransactions = transactions.filter { transaction ->
        (selectedCategories.value.isEmpty() || selectedCategories.value.contains(transaction.category)) &&
                (selectedTypes.value.isEmpty() || selectedTypes.value.contains(transaction.transactionType))
    }


    Column(modifier = modifier) {
        HomeHeader(
            headerColor,
            leftItem = {
                BaseIconButton(
                    onClick = {
                        viewModel.navigateBack()
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
                        painter = painterResource(R.drawable.chevron_left),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .aspectRatio(981f / 378f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = modifier
                        .weight(1f)
                        .align(Alignment.Bottom),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Current balance",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "$${card.balance}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        IconButton(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.CenterVertically),
                            onClick = {
                                isNumberVisible.value = !isNumberVisible.value
                            },
                        ) {
                            Icon(
                                painter = if (isNumberVisible.value)
                                    painterResource(R.drawable.eye) else
                                    painterResource(R.drawable.eye_off),
                                contentDescription = if (isNumberVisible.value)
                                    stringResource(R.string.hide) else stringResource(R.string.show),
                                tint = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }

                    val cardNumber = if (isNumberVisible.value) card.cardNumber else
                        CardUtils.maskCardNumber(card.cardNumber)

                    Text(
                        text = "Bank account: ${
                            CardUtils.formatCardNumberWithSpaces(
                                cardNumber
                            )
                        }",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                BaseIconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically),
                    onClick = {
                        viewModel.navigateToSelectCard()
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.change),
                        contentDescription = "Change card",
                        tint = Color.Unspecified
                    )
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionsFilterRow(
                selectedCategories = selectedCategories.value,
                onCategoryToggle = { category ->
                    selectedCategories.value = selectedCategories.value.toMutableSet().apply {
                        if (!add(category)) remove(category)
                    }
                },
                onCategoryClear = { selectedCategories.value = emptySet() },
                selectedTypes = selectedTypes.value,
                onTypeToggle = { type ->
                    selectedTypes.value = selectedTypes.value.toMutableSet().apply {
                        if (!add(type)) remove(type)
                    }
                },
                onTypeClear = { selectedTypes.value = emptySet() }
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Spacer(Modifier.height(8.dp))

                GroupedTransactionsList(
                    cardTransactions = filteredTransactions,
                    profile = profile,
                    card = card
                )
            }
        }
    }
}

fun NavGraphBuilder.transactionsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.TransactionsScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: HomeViewModel = hiltViewModel(parentEntry)

        TransactionsScreen(
            modifier = modifier,
            viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionsScreenPreview() {
    TransactionsScreen(
        Modifier.fillMaxSize(),
    )
}
