package com.aiinty.copayment.presentation.ui._components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.presentation.common.CardUtils
import com.aiinty.copayment.presentation.ui._components.card.styles.classic.CardBotClassic
import com.aiinty.copayment.presentation.ui._components.card.styles.minimal.CardBotMinimal
import com.aiinty.copayment.presentation.ui._components.card.styles.split.CardBotSplit

@Composable
fun BaseCardBot(
    modifier: Modifier = Modifier,
    card: Card,
    showBalance: Boolean = false,
) {
    val displayedExpiry = CardUtils.formatExpiryToSlash(card.expirationDate)
    val formattedCard = card.copy(
        expirationDate = displayedExpiry
    )

    Box(
        modifier = modifier
            .aspectRatio(981f / 192f)
            .fillMaxWidth()
    ) {
        when (card.cardStyle) {
            CardStyle.CLASSIC -> CardBotClassic(
                card = formattedCard,
                showBalance = showBalance
            )

            CardStyle.SPLIT -> CardBotSplit(
                card = formattedCard,
                showBalance = showBalance
            )

            CardStyle.MINIMAL -> CardBotMinimal(
                card = formattedCard,
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
            userId = "1",
            cardNumber = "1234567890123456",
            cardHolderName = "John Doe",
            expirationDate = "1324",
            cvv = "123",
            balance = 1000.0,
            cardStyle = CardStyle.CLASSIC
        ),
        showBalance = true
    )
}
