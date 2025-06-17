package com.aiinty.copayment.presentation.ui.main.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.auth.FullNameTextField
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.card.BaseCard
import com.aiinty.copayment.presentation.ui._components.card.CardCvvTextField
import com.aiinty.copayment.presentation.ui._components.card.CardExpiryTextField
import com.aiinty.copayment.presentation.ui._components.card.CardNumberTextField
import com.aiinty.copayment.presentation.ui.theme.Greyscale100
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.CardUiState
import com.aiinty.copayment.presentation.viewmodels.CardViewModel

@Composable
fun CreateCardScreen(
    modifier: Modifier = Modifier,
    viewModel: CardViewModel = hiltViewModel(),
    cardStyle: CardStyle,
) {
    UiErrorHandler(viewModel)

    val uiState = viewModel.uiState

    val cardNumber = remember { mutableStateOf("") }
    val cardNumberError = remember { mutableStateOf<Int?>(null) }
    val expiryDate = remember { mutableStateOf("") }
    val expiryDateError = remember { mutableStateOf<Int?>(null) }
    val cvv = remember { mutableStateOf("") }
    val cvvError = remember { mutableStateOf<Int?>(null) }
    val cardholderName = remember { mutableStateOf("") }
    val cardholderNameError = remember { mutableStateOf<Int?>(null) }

    val cardMock = Card(
        id = "",
        userId = "1",
        cardNumber = cardNumber.value,
        cardHolderName = cardholderName.value,
        expirationDate = expiryDate.value,
        cvv = cvv.value,
        balance = 0.0,
        cardStyle = cardStyle
    )

    val isInputsValidated = cardNumberError.value == null && expiryDateError.value == null &&
            cvvError.value == null && cardholderNameError.value == null &&
            cardNumber.value.isNotEmpty() && expiryDate.value.isNotEmpty() &&
            cvv.value.isNotEmpty() && cardholderName.value.isNotEmpty()

    when(uiState.value) {
        is CardUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is CardUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.something_went_wrong)
                )
            }
        }

        is CardUiState.Success -> {
            Column(
                modifier = modifier,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Greyscale100)
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                ) {
                    BaseCard(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                            ),
                        card = cardMock,
                        showCardNumber = true
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    CreateCardFields(
                        cardNumber = cardNumber,
                        cardNumberError = cardNumberError,
                        expiryDate = expiryDate,
                        expiryDateError = expiryDateError,
                        cvv = cvv,
                        cvvError = cvvError,
                        cardholderName = cardholderName,
                        cardholderNameError = cardholderNameError
                    )

                    BaseButton(
                        onClick = {
                            if (isInputsValidated) {
                                viewModel.insertCard(
                                    card = cardMock
                                )
                            }
                        },
                        enabled = isInputsValidated
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateCardFields(
    modifier: Modifier = Modifier,
    cardNumber: MutableState<String>,
    cardNumberError: MutableState<Int?>,
    expiryDate: MutableState<String>,
    expiryDateError: MutableState<Int?>,
    cvv: MutableState<String>,
    cvvError: MutableState<Int?>,
    cardholderName: MutableState<String>,
    cardholderNameError: MutableState<Int?>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardNumberTextField(
            modifier = Modifier.fillMaxWidth(),
            cardNumber = cardNumber.value,
            onCardNumberChange = { cardNumber.value = it },
            onValidationResultChange = { error ->
                cardNumberError.value = error
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardExpiryTextField(
                modifier = Modifier.weight(1f),
                expiry = expiryDate.value,
                onExpiryChange = { expiryDate.value = it },
                onValidationResultChange = { error ->
                    expiryDateError.value = error
                },
                showError = false
            )

            CardCvvTextField(
                modifier = Modifier.weight(1f),
                cvv = cvv.value,
                onCvvChange = { cvv.value = it },
                onValidationResultChange = { error ->
                    cvvError.value = error
                },
                showError = false
            )
        }

        var expiryAndCvvError: Int? = null
        if (expiryDateError.value != null) {
            expiryAndCvvError = expiryDateError.value
        }
        if (cvvError.value != null) {
            expiryAndCvvError = cvvError.value
        }
        expiryAndCvvError?.let { error ->
            Text(
                text = stringResource(error),
                color = Color.Red,
                style = Typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        FullNameTextField(
            modifier = Modifier.fillMaxWidth(),
            fullName = cardholderName.value,
            onFullNameChange = { cardholderName.value = it },
            labelResId = R.string.cardholder_name,
            validateRules = { name ->
                val pattern = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$".toRegex()
                when {
                    name.isBlank() -> R.string.error_holder_name_blank
                    !pattern.matches(name) -> R.string.error_holder_name_length
                    else -> null
                }
            },
            onValidationResultChange = { error ->
                cardholderNameError.value = error
            }
        )
    }
}

fun NavGraphBuilder.createCardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    composable(
        route = "create_card?style={style}",
        arguments = listOf(
            navArgument("style") { type = NavType.StringType },
        )
    ){ backStackEntry ->
        val styleOrdinal = backStackEntry.arguments?.getString("style")?.toIntOrNull()
        val cardStyle = CardStyle.entries[styleOrdinal ?: 0]

        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.CardGraph.route)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)

        CreateCardScreen(
            modifier = modifier,
            cardStyle = cardStyle,
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateCardScreenPreview() {
    CreateCardScreen(
        modifier = Modifier.fillMaxSize(),
        cardStyle = CardStyle.CLASSIC
    )
}
