package com.aiinty.copayment.domain.usecase.home

import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.repository.TransactionRepository

class InsertTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        return transactionRepository.insertTransaction(transaction)
    }
}
