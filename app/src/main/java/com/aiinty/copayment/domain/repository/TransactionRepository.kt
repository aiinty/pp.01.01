package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(cardId: String?, range: String): Result<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Result<Unit>
}