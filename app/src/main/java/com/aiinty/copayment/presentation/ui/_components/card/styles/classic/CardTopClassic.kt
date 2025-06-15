package com.aiinty.copayment.presentation.ui._components.card.styles.classic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card

@Composable
fun BoxScope.CardTopClassic(
    card: Card
) {
    Image(
        painter = painterResource(id = R.drawable.card_top_0),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = card.cardNumber,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = card.expirationDate,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
    }

}
