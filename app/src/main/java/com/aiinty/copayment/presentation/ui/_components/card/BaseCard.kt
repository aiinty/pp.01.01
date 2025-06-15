package com.aiinty.copayment.presentation.ui._components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    card: Card,
    showBalance: Boolean = false,
    aspectRatio: Float = 981f / 570f
) {
    Column(
        modifier = modifier
            .aspectRatio(aspectRatio)
    ) {
        BaseCardTop(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            card = card
        )
        BaseCardBot(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            card = card,
            showBalance = showBalance
        )
    }
}

@Preview
@Composable
private fun BaseCardPreview() {
    BaseCard(
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
        showBalance = false
    )
}
