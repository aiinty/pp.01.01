package com.aiinty.copayment.presentation.ui._components.card.styles.classic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle

@Composable
fun CardBotClassic(
    modifier: Modifier = Modifier,
    card: Card,
    showBalance: Boolean = false
) {
    Box(
        modifier = modifier
            .size(width = 327.dp, height = 64.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.card_bot_0),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 20.dp)
        ) {
            if (showBalance) {
                Text(
                    text = "$${card.balance.toString()}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = card.cardHolderName,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

        }
    }
}

@Preview
@Composable
private fun CardBotClassicPreview() {
    CardBotClassic(
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
