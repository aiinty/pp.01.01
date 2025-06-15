package com.aiinty.copayment.domain.usecase.card

import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.repository.CardRepository

class GetCardsUseCase(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(userId: String): Result<List<Card>> {
        return cardRepository.getCards(userId)
    }
}
