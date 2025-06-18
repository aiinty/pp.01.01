package com.aiinty.copayment.presentation.ui.main.qr

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.profile.ContactProfileItem
import com.aiinty.copayment.presentation.ui._components.qr.QrCodeImage
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Greyscale400
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.utils.CardUtils
import com.aiinty.copayment.presentation.viewmodels.QRUiState
import com.aiinty.copayment.presentation.viewmodels.QRViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRCodeScreen(
    modifier: Modifier = Modifier,
    viewModel: QRViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
    }

    when(val state = viewModel.uiState.value) {
        is QRUiState.Loading -> LoadingScreen(modifier)
        is QRUiState.Error -> ErrorScreen(modifier)
        is QRUiState.Success ->
            QRCodeScreenContent(
                modifier,
                viewModel,
                state.profile,
                state.card
            )
    }
}

@Composable
private fun QRCodeScreenContent(
    modifier: Modifier,
    viewModel: QRViewModel,
    profile: Profile,
    card: Card
) {
    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            viewModel.decodeQrContent(result.contents)
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.navigateToSelectCard()
                }
                .background(Greyscale50, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactProfileItem(profile = profile, cardNumber = CardUtils.maskCardNumber(card.cardNumber))

            Icon(
                modifier = Modifier
                    .size(20.dp),
                painter = painterResource(R.drawable.chevron_bottom),
                contentDescription = null,
                tint = Greyscale400
            )
        }

        Box(
            Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .size(300.dp)
        ) {
            QrCodeImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                text = viewModel.generateQrCodeInfo(profile, card),
            )
        }

        BaseButton(
            onClick = {
                scanLauncher.launch(
                    ScanOptions().apply {
                        setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        setPrompt("Scan QR-code")
                        setOrientationLocked(false)
                    }
                )
            }) {
            Text("Scan QR")
        }
    }
}

fun NavGraphBuilder.qrCodeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    composable(
        route = NavigationRoute.QRCodeScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: QRViewModel = hiltViewModel(parentEntry)

        QRCodeScreen(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QRCodeScreenPreview() {
    QRCodeScreen(
        Modifier.fillMaxSize()
    )
}
