package com.aiinty.copayment.domain.usecase.home

import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.repository.TransactionRepository

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(cardId: String?, range: String = "0-9"): Result<List<Transaction>> {
        return transactionRepository.getTransactions(cardId, range)
    }
}
