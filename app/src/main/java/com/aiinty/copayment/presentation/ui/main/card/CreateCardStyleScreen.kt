package com.aiinty.copayment.presentation.ui.main.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.card.BaseCard
import kotlinx.coroutines.launch

@Composable
fun CreateCardStyleScreen(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus,
) {
    val scope = rememberCoroutineScope()
    val cardMockup = Card(
        id = "",
        userId = "1",
        cardNumber = "2564854684211121",
        cardHolderName = "Tommy Jason",
        expirationDate = "1226",
        cvv = "",
        balance = 0.0,
        cardStyle = CardStyle.CLASSIC
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CardStyle.entries.forEach { cardStyle ->
            BaseCard(
                modifier = Modifier.clickable {
                    scope.launch {
                        navigationEventBus.send(
                            NavigationEvent.ToRoute(
                                NavigationRoute.CreateCardScreen(cardStyle).route
                            )
                        )
                    }
                },
                card = cardMockup.copy(cardStyle = cardStyle),
                showCardNumber = true
            )
        }
    }
}

fun NavGraphBuilder.createCardStyleScreen(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus
) {
    composable(
        route = NavigationRoute.CreateCardStyleScreen.route
    ){
        CreateCardStyleScreen(
            modifier = modifier,
            navigationEventBus
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateCardStyleScreenPreview() {
    CreateCardStyleScreen(
        Modifier.fillMaxSize(),
        NavigationEventBus()
    )
}
