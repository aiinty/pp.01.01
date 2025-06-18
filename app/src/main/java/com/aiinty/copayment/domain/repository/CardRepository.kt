package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.data.model.card.MaskedCardResponse
import com.aiinty.copayment.domain.model.Card

interface CardRepository {
    suspend fun getCards(userId: String): Result<List<Card>>
    suspend fun insertCard(card: Card): Result<Unit>
    suspend fun updateCard(card: Card): Result<Unit>
    suspend fun getMaskedCards(cardIds: List<String>): Result<List<MaskedCardResponse>>
}