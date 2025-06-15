package com.aiinty.copayment.presentation.ui._components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.presentation.ui._components.card.styles.classic.CardTopClassic
import com.aiinty.copayment.presentation.ui._components.card.styles.minimal.CardTopMinimal
import com.aiinty.copayment.presentation.ui._components.card.styles.split.CardTopSplit

@Composable
fun BaseCardTop(
    modifier: Modifier = Modifier,
    card: Card
) {
    Box(
        modifier = modifier
            .aspectRatio(981f / 378f)
            .fillMaxWidth()
    ) {
        when (card.cardStyle) {
            CardStyle.CLASSIC -> CardTopClassic(
                card = card
            )

            CardStyle.SPLIT -> CardTopSplit(
                card = card
            )

            CardStyle.MINIMAL -> CardTopMinimal(
                card = card
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
            cardNumber = "1234 5678 9012 3456",
            cardHolderName = "John Doe",
            expirationDate = "13/24",
            cvv = "123",
            isActive = true,
            balance = 1000.0,
            cardStyle = CardStyle.CLASSIC
        )
    )
}
