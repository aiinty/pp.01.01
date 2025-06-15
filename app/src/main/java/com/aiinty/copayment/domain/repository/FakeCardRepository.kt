package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Card
import kotlinx.coroutines.delay
import java.io.File

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
}
