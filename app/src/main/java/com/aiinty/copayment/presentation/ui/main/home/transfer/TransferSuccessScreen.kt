package com.aiinty.copayment.presentation.ui.main.home.transfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.viewmodels.TransferUiState
import com.aiinty.copayment.presentation.viewmodels.TransferViewModel
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@Composable
fun TransferSuccessScreen(
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel = hiltViewModel()
) {
    when(val state = viewModel.uiState.value) {
        TransferUiState.Loading,
        is TransferUiState.EnterAmount,
        is TransferUiState.SelectCardAndContact -> LoadingScreen(modifier)
        is TransferUiState.Success -> TransferSuccessScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            amount = state.amount
        )
        TransferUiState.Error -> ErrorScreen(modifier)
    }
}

@Composable
fun TransferSuccessScreenContent(
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel,
    amount: Double
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(R.drawable.transfer_successful),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.transfer_success_title),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF1D2B4F)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.transfer_success_desc),
            color = Color(0xFF7C8193),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .background(Greyscale50, RoundedCornerShape(16.dp))
                .padding(horizontal = 40.dp, vertical = 20.dp)
        ) {
            Text(
                text = "$${"%.2f".format(amount)}",
                color = Greyscale900
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BaseButton (
            onClick = {
                viewModel.navigateToHome()
            },
        ) {
            Text(
                text = "Back to Home"
            )
        }
    }
}

fun NavGraphBuilder.transferSuccessScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.TransferProofScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: TransferViewModel = hiltViewModel(parentEntry)

        TransferSuccessScreen(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransferSuccessScreenPreview() {
    TransferSuccessScreen(
        Modifier.fillMaxSize(),
    )
}