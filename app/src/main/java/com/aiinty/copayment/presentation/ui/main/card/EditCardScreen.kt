package com.aiinty.copayment.presentation.ui.main.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.base.BaseSwitch
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.card.BaseCard
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale100
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.CardUiState
import com.aiinty.copayment.presentation.viewmodels.CardViewModel

@Composable
fun EditCardScreen(
    modifier: Modifier = Modifier,
    viewModel: CardViewModel = hiltViewModel(),
) {
    UiErrorHandler(viewModel)

    val uiState = viewModel.uiState
    val card = viewModel.selectedCard.collectAsState().value

    if (card == null) {
        viewModel.selectedCardIsNull()
        return
    }

    val freezePhysical = remember { mutableStateOf(card.isFrozen) }
    val disableContactless = remember { mutableStateOf(card.isContactlessDisabled) }
    val disableMagstripe = remember { mutableStateOf(card.isMagstripeDisabled) }

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
                        .weight(1f)
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
                        card = card,
                        showCardNumber = true
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {

                    Column(
                        modifier = Modifier.padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SettingItem(
                            checked = freezePhysical,
                            iconResId = R.drawable.card,
                            iconTintColor = Color.Black,
                            label = stringResource(R.string.freeze_card)
                        )

                        HorizontalDivider(thickness = 1.dp, color = Greyscale100)

                        SettingItem(
                            checked = disableContactless,
                            iconResId = R.drawable.contactless,
                            iconTintColor = Greyscale900,
                            label = stringResource(R.string.disable_contactless)
                        )

                        HorizontalDivider(thickness = 1.dp, color = Greyscale100)

                        SettingItem(
                            checked = disableMagstripe,
                            iconResId = R.drawable.lock,
                            iconTintColor = Green,
                            label = stringResource(R.string.disable_magstripe)
                        )
                    }

                    BaseButton(
                        onClick = {
                            val cardWithSettings = card.copy(
                                isFrozen = freezePhysical.value,
                                isContactlessDisabled = disableContactless.value,
                                isMagstripeDisabled = disableMagstripe.value
                            )

                            viewModel.updateCard(cardWithSettings)
                        }
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
private fun SettingItem(
    modifier: Modifier = Modifier,
    checked: MutableState<Boolean>,
    iconResId: Int?,
    iconTintColor: Color = Green,
    label: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Greyscale50, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (iconResId != null) painterResource(iconResId) else
                        painterResource(R.drawable.settings),
                    contentDescription = label,
                    tint = iconTintColor
                )
            }

            Text(
                text = label,
                color = Greyscale900,
                style = Typography.bodyMedium,
                fontWeight = FontWeight.W500
            )
        }

        BaseSwitch(
            checked = checked.value,
            onCheckedChange = { checked.value = it }
        )
    }
}


fun NavGraphBuilder.editCardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.EditCardScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.CardGraph.route)
        }
        val viewModel: CardViewModel = hiltViewModel(parentEntry)

        EditCardScreen(
            modifier = modifier,
            viewModel = viewModel,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditCardScreenPreview() {
    EditCardScreen(
        modifier = Modifier.fillMaxSize(),
    )
}
