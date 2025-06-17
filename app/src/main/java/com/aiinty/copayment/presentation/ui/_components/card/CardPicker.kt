package com.aiinty.copayment.presentation.ui._components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiinty.copayment.domain.model.Card

fun LazyListScope.CardPicker(
    cards: List<Card>,
    onSelect: (Card) -> Unit
) {
    items(cards) { card ->
        BaseCard(
            modifier = Modifier
                .clickable {
                    onSelect(card)
                },
            card = card
        )
    }
}
