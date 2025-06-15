package com.aiinty.copayment.domain.usecase.card

import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.repository.CardRepository

class InsertCardUseCase(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(card: Card): Result<Unit> {
        return cardRepository.insertCard(card)
    }
}
