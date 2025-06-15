package com.aiinty.copayment.presentation.ui._components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.presentation.ui._components.card.styles.classic.CardBotClassic
import com.aiinty.copayment.presentation.ui._components.card.styles.minimal.CardBotMinimal
import com.aiinty.copayment.presentation.ui._components.card.styles.split.CardBotSplit

@Composable
fun BaseCardBot(
    modifier: Modifier = Modifier,
    card: Card,
    showBalance: Boolean = false,
) {
    Box(
        modifier = modifier
            .aspectRatio(981f / 192f)
            .fillMaxWidth()
    ) {
        when (card.cardStyle) {
            CardStyle.CLASSIC -> CardBotClassic(
                card = card,
                showBalance = showBalance
            )

            CardStyle.SPLIT -> CardBotSplit(
                card = card,
                showBalance = showBalance
            )

            CardStyle.MINIMAL -> CardBotMinimal(
                card = card,
                showBalance = showBalance
            )
        }
    }
}

@Preview
@Composable
private fun BaseCardBotPreview() {
    BaseCardBot(
        card = Card(
            id = "1",
            cardNumber = "1234 5678 9012 3456",
            cardHolderName = "John Doe",
            expirationDate = "13/24",
            cvv = "123",
            isActive = true,
            balance = 1000.0,
            cardStyle = CardStyle.CLASSIC
        ),
        showBalance = true
    )
}
