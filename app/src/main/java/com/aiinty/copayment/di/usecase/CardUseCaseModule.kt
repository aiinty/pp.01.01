package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.CardRepository
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.card.InsertCardUseCase
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

}
