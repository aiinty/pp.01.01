package com.aiinty.copayment.presentation.ui.main.home.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.utils.MoneyUtils.formatMoney
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.BaseTextField
import com.aiinty.copayment.presentation.ui._components.base.NumericKeyboard
import com.aiinty.copayment.presentation.ui._components.profile.ProfileAvatar
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale200
import com.aiinty.copayment.presentation.ui.theme.Greyscale400
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.viewmodels.TransferUiState
import com.aiinty.copayment.presentation.viewmodels.TransferViewModel

@Composable
fun TransferAmountScreen(
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel = hiltViewModel(),
) {
    when(val state = viewModel.uiState.value) {
        TransferUiState.Loading,
        is TransferUiState.SelectCardAndContact,
        is TransferUiState.Success -> LoadingScreen(modifier)
        is TransferUiState.EnterAmount -> TransferAmountScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            card = state.card,
            contact = state.contact
        )
        TransferUiState.Error -> ErrorScreen(modifier)
    }
}

@Composable
fun TransferAmountScreenContent(
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel,
    card: Card,
    contact: Contact
) {
    val maxAmount = card.balance

    var input = remember { mutableStateOf("") }
    val isButtonEnabled = input.value.toDoubleOrNull()?.let { it in 0.01..maxAmount } == true

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        ProfileAvatar(
            avatarUrl = contact.profile.fullAvatarUrl
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "to ${contact.profile.fullName}",
            style = MaterialTheme.typography.titleMedium,
            color = Greyscale900,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Greyscale200, RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.enter_amount),
                        color = Color(0xFFB0B4C3),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.max, maxAmount.formatMoney()),
                        color = Color(0xFFB0B4C3),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                BaseTextField(
                    value = input.value,
                    onValueChange = { input.value = it },
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Green,
                        focusedContainerColor = Greyscale50,
                        unfocusedContainerColor = Greyscale50,
                        selectionColors = TextSelectionColors(Green, Green.copy(alpha = 0.25f)),
                        focusedLabelColor = Greyscale400,
                        unfocusedLabelColor = Greyscale400,
                        focusedTextColor = Greyscale900,
                        unfocusedTextColor = Greyscale900,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))

        BaseButton (
            onClick = { viewModel.onAmountEntered(amount = input.value.toDouble()) },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(R.string.send_money))
        }

        Spacer(modifier = Modifier.height(20.dp))

        NumericKeyboard(
            input = input,
            validateRules = {
                it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))
            }
        )
    }
}

fun NavGraphBuilder.transferAmountScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.TransferAmountScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: TransferViewModel = hiltViewModel(parentEntry)

        TransferAmountScreen(
            modifier = modifier,
            viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnterAmountScreenPreview() {
    TransferAmountScreen(
        Modifier.fillMaxSize(),
    )
}
