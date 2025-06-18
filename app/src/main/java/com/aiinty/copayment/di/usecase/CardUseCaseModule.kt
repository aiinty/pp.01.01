package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.CardRepository
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.card.GetMaskedCardsUseCase
import com.aiinty.copayment.domain.usecase.card.InsertCardUseCase
import com.aiinty.copayment.domain.usecase.card.UpdateCardUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CardUseCaseModule {

    @Provides
    fun provideGetCardsUseCase(
        cardRepository: CardRepository
    ): GetCardsUseCase {
        return GetCardsUseCase(cardRepository)
    }

    @Provides
    fun provideInsertCardUseCase(
        cardRepository: CardRepository
    ): InsertCardUseCase {
        return InsertCardUseCase(cardRepository)
    }

    @Provides
    fun provideUpdateCardUseCase(
        cardRepository: CardRepository
    ): UpdateCardUseCase {
        return UpdateCardUseCase(cardRepository)
    }

    @Provides
    fun provideGetMaskedCardUseCase(
        cardRepository: CardRepository
    ): GetMaskedCardsUseCase {
        return GetMaskedCardsUseCase(cardRepository)
    }

}
