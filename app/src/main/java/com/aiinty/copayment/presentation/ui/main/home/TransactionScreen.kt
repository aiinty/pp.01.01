package com.aiinty.copayment.presentation.ui.main.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.domain.utils.MoneyUtils.formatMoney
import com.aiinty.copayment.domain.utils.MoneyUtils.formatMoneyInt
import com.aiinty.copayment.domain.utils.MoneyUtils.toDoubleClean
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.NumericKeyboard
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale400
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.WithdrawUiState
import com.aiinty.copayment.presentation.viewmodels.TransactionViewModel

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = hiltViewModel(),
    transactionType: TransactionType = TransactionType.WITHDRAW
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
    }

    when (val state = viewModel.uiState.value) {
        WithdrawUiState.Loading,
        WithdrawUiState.Success -> LoadingScreen(modifier)
        WithdrawUiState.Error -> ErrorScreen(modifier)
        is WithdrawUiState.EnterAmount -> TransactionScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            card = state.card,
            transactionType = transactionType
        )
    }
}

@Composable
fun TransactionScreenContent(
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel,
    card: Card,
    transactionType: TransactionType
) {
    val isWithdrawLike = TransactionType.isWithdrawLike(transactionType)
    val maxAmount = if(isWithdrawLike) card.balance else Double.MAX_VALUE
    val input = remember { mutableStateOf("") }
    val selectedPercent = remember { mutableStateOf<Int?>(null) }

    val isButtonEnabled = input.value.toDoubleClean()?.let { it in 0.01..maxAmount } == true

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Box(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$",
                    color = Greyscale500,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = input.value.ifEmpty { "0" },
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Greyscale900,
                    modifier = Modifier.wrapContentWidth(),
                    maxLines = 1
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        if (isWithdrawLike) {
            Text(
                text = stringResource(R.string.maximum, maxAmount.formatMoney()),
                color = Greyscale400,
            )

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val percents = listOf(25, 50, 75, 100)
                percents.forEach { percent ->
                    val isSelected = percent == selectedPercent.value
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .background(
                                color = if (isSelected) Green else Greyscale50,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedPercent.value = percent
                                input.value = ((maxAmount * percent) / 100).formatMoneyInt()
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "$percent%",
                            color = if (isSelected) Color.White else Greyscale900,
                            style = Typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        } else {
            Spacer(Modifier.height(128.dp))
        }

        Button(
            onClick = {
                input.value.toDoubleClean()?.let {
                    viewModel.onAmountEntered(card, it, transactionType)
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Greyscale900,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(TransactionType.toResId(transactionType)))
        }
        Spacer(Modifier.height(24.dp))

        NumericKeyboard(
            input = input,
            validateRules = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                    selectedPercent.value = null
                    true
                } else false
            }
        )
    }
}

fun NavGraphBuilder.transactionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = "transaction/{type}",
        arguments = listOf(
            navArgument("type") { type = NavType.IntType }
        )
    ){backStackEntry ->
        val typeId = backStackEntry.arguments?.getInt("type")
        val type = if (typeId != null) TransactionType.fromId(typeId) else null
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: TransactionViewModel = hiltViewModel(parentEntry)

        if (type != null) {
            TransactionScreen(
                modifier = modifier,
                viewModel = viewModel,
                transactionType = type
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawScreenPreview() {
    TransactionScreen(
        Modifier.fillMaxSize(),
    )
}
