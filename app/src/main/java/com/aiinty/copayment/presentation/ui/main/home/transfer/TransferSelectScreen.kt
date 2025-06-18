package com.aiinty.copayment.presentation.ui.main.home.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui._components.card.BaseCard
import com.aiinty.copayment.presentation.ui._components.profile.ProfileAvatar
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale200
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.viewmodels.TransferUiState
import com.aiinty.copayment.presentation.viewmodels.TransferViewModel

@Composable
fun TransferSelectScreen(
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
    }

    when(val state = viewModel.uiState.value) {
        TransferUiState.Loading,
        is TransferUiState.EnterAmount,
        is TransferUiState.Success -> LoadingScreen(modifier)
        is TransferUiState.SelectCardAndContact -> TransferSelectScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            cards = state.cards,
            contacts = state.contacts
        )
        TransferUiState.Error -> ErrorScreen(modifier)
    }
}

@Composable
private fun TransferSelectScreenContent(
    viewModel: TransferViewModel,
    modifier: Modifier,
    cards: List<Card>,
    contacts: List<Contact>,
) {
    val selectedCard = viewModel.selectedCard.collectAsState()
    val selectedContact = viewModel.selectedContact.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(modifier.weight(2f)) {
            Text(
                text = stringResource(R.string.choose_cards),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier.width(16.dp))
                }

                items(cards) { card ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        val isSelected = selectedCard.value == card
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .size(24.dp)
                                    .background(
                                        color = if (card.cardStyle == CardStyle.MINIMAL) Greyscale900 else Green,
                                        shape = CircleShape
                                    )
                                    .zIndex(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                            }
                        }

                        BaseCard(
                            modifier = Modifier
                                .zIndex(0f)
                                .clickable { viewModel.selectCard(card) },
                            card = card
                        )
                    }
                }

                item {
                    Spacer(modifier.width(16.dp))
                }
            }
        }
        Column(modifier.weight(2f)) {
            Text(
                text = stringResource(R.string.choose_recipients),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier.width(16.dp))
                }

                items(contacts) { contact ->
                    RecipientCard(
                        contact = contact,
                        isSelected = selectedContact.value == contact,
                        onClick = { viewModel.selectContact(contact) }
                    )
                }

                item {
                    Spacer(modifier.width(16.dp))
                }
            }
        }

        Spacer(modifier.weight(1f))

        BaseButton(
            modifier = Modifier.padding(16.dp),
            onClick = {
                if (selectedCard.value != null && selectedContact.value != null) {
                    viewModel.onCardAndContactSelected(selectedCard.value!!, selectedContact.value!!)
                }
            },
            enabled = selectedCard.value != null && selectedContact.value != null,
        ) {
            Text(stringResource(R.string.continue_text))
        }
    }
}

@Composable
fun RecipientCard(
    contact: Contact,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(130f / 154f)
            .fillMaxSize()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Green else Greyscale200,
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.check),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp),
                tint = Green
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileAvatar(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                avatarUrl = contact.profile.fullAvatarUrl,
                withBorder = false
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = contact.profile.fullName,
                style = MaterialTheme.typography.bodyMedium,
                color = Greyscale900
            )
        }
    }
}

fun NavGraphBuilder.transferSelectScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.TransferSelectScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: TransferViewModel = hiltViewModel(parentEntry)

        TransferSelectScreen(
            modifier = modifier,
            viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransferSelectScreenPreview() {
    TransferSelectScreen(
        Modifier.fillMaxSize(),
    )
}

