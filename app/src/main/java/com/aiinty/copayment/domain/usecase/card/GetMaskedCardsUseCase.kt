package com.aiinty.copayment.domain.usecase.card

import com.aiinty.copayment.data.model.card.MaskedCardResponse
import com.aiinty.copayment.domain.repository.CardRepository

class GetMaskedCardsUseCase(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(cardIds: List<String>): Result<List<MaskedCardResponse>> {
        return cardRepository.getMaskedCards(cardIds)
    }
}
