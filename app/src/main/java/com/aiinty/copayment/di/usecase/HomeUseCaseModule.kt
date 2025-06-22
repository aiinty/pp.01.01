package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.TransactionRepository
import com.aiinty.copayment.domain.usecase.home.GetTransactionsUseCase
import com.aiinty.copayment.domain.usecase.home.InsertTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {

    @Provides
    fun provideGetTransactionsUseCase(
        transactionRepository: TransactionRepository
    ): GetTransactionsUseCase {
        return GetTransactionsUseCase(transactionRepository)
    }

    @Provides
    fun provideInsertTransactionUseCase(
        transactionRepository: TransactionRepository
    ): InsertTransactionUseCase {
        return InsertTransactionUseCase(transactionRepository)
    }

}
