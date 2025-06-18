package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.data.model.card.MaskedCardResponse
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.presentation.utils.CardUtils
import kotlinx.coroutines.delay

class FakeCardRepository : CardRepository {

    private val cards = mutableListOf<Card>()

    override suspend fun getCards(userId: String): Result<List<Card>> {
        delay(500)
        return Result.success(cards.filter { it.userId == userId })
    }

    override suspend fun insertCard(card: Card): Result<Unit> {
        delay(500)
        cards.add(card)
        return Result.success(Unit)
    }

    override suspend fun updateCard(card: Card): Result<Unit> {
        delay(500)
        val index = cards.indexOfFirst { it.id == card.id }
        return if (index != -1) {
            cards[index] = card
            Result.success(Unit)
        } else {
            Result.failure(Exception("Card not found"))
        }
    }

    override suspend fun getMaskedCards(cardIds: List<String>): Result<List<MaskedCardResponse>> {
        delay(500)
        val responses = cardIds.map { cardId ->
            val card = cards.find { it.id == cardId }
            if (card != null) {
                MaskedCardResponse(
                    card_id = cardId,
                    masked_number = CardUtils.maskCardNumber(card.cardNumber)
                )
            } else {
                MaskedCardResponse(
                    card_id = cardId,
                    masked_number = null
                )
            }
        }
        return Result.success(responses)
    }
}
