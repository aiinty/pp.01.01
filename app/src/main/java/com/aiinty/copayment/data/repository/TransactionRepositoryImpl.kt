package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.transaction.TransactionInsertRequest
import com.aiinty.copayment.data.network.TransactionApi
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.domain.repository.TransactionRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi,
    private val gson: Gson,
    private val prefs: UserPreferences,
) :  BaseRepositoryImpl(gson), TransactionRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val bearerToken = { "Bearer ${prefs.getAccessToken()}" }

    override suspend fun getTransactions(
        userId: String?,
        cardId: String?,
        range: String,
    ): Result<List<Transaction>> {
        return withContext(ioDispatcher) {
            val response = api.getTransactions(
                or = "(sender_id.eq.$cardId,receiver_id.eq.$cardId,initiator_user_id.eq.$userId)",
                authHeader = bearerToken.invoke(),
                range = range
            )

            handleApiResponse(response).fold(
                onSuccess = { trxList ->
                    val cards: List<Transaction> = trxList.map { trx ->
                        Transaction(
                            id = trx.id,
                            senderId = trx.sender_id,
                            receiverId = trx.receiver_id,
                            amount = trx.amount,
                            createdAt = trx.created_at,
                            initiatorUserId = trx.initiator_user_id,
                            transactionType = TransactionType.fromId(trx.type) ?: TransactionType.OTHER
                        )
                    }
                    Result.success(cards)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }
    }

    override suspend fun insertTransaction(transaction: Transaction): Result<Unit> {
        return withContext(ioDispatcher) {
            val requestBody = TransactionInsertRequest(
                type_id = transaction.transactionType.id,
                amount = transaction.amount,
                sender_id = transaction.senderId,
                receiver_id = transaction.receiverId,
                initiator_user_id = transaction.initiatorUserId
            )

            val response = api.insertTransaction(
                authHeader = bearerToken.invoke(),
                body = requestBody
            )
            handleEmptyResponse(response)
        }
    }

}
