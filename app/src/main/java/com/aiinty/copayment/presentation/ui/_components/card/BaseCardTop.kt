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
import com.aiinty.copayment.presentation.ui._components.card.styles.classic.CardTopClassic
import com.aiinty.copayment.presentation.ui._components.card.styles.minimal.CardTopMinimal
import com.aiinty.copayment.presentation.ui._components.card.styles.split.CardTopSplit

@Composable
fun BaseCardTop(
    modifier: Modifier = Modifier,
    card: Card,
    showCardNumber: Boolean = false
) {
    val displayedCardNumber = if (showCardNumber) {
        CardUtils.formatCardNumberWithSpaces(card.cardNumber)
    } else {
        CardUtils.formatCardNumberWithSpaces(CardUtils.maskCardNumber(card.cardNumber))
    }
    val displayedExpiry = CardUtils.formatExpiryToSlash(card.expirationDate)
    val formattedCard = card.copy(
        cardNumber = displayedCardNumber,
        expirationDate = displayedExpiry
    )

    Box(
        modifier = modifier
            .aspectRatio(981f / 378f)
            .fillMaxWidth()
    ) {
        when (card.cardStyle) {
            CardStyle.CLASSIC -> CardTopClassic(
                card = formattedCard
            )

            CardStyle.SPLIT -> CardTopSplit(
                card = formattedCard
            )

            CardStyle.MINIMAL -> CardTopMinimal(
                card = formattedCard
            )
        }
    }
}

@Preview
@Composable
private fun BaseCardTopPreview() {
    BaseCardTop(
        card = Card(
            id = "1",
            userId = "1",
            cardNumber = "1234567890123456",
            cardHolderName = "John Doe",
            expirationDate = "1324",
            cvv = "123",
            balance = 1000.0,
            cardStyle = CardStyle.CLASSIC
        )
    )
}
