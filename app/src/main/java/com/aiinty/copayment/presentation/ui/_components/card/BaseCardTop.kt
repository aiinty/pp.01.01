package com.aiinty.copayment.presentation.ui._components.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    when(card.cardStyle) {
        CardStyle.CLASSIC -> CardTopClassic(
            modifier = modifier,
            card = card
        )
        CardStyle.SPLIT -> CardTopSplit(
            modifier = modifier,
            card = card
        )
        CardStyle.MINIMAL -> CardTopMinimal(
            modifier = modifier,
            card = card
        )
    }
}