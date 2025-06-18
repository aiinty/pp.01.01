package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.transaction.TransactionInsertRequest
import com.aiinty.copayment.data.network.TransactionApi
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.domain.repository.TransactionRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi,
    private val gson: Gson,
    private val prefs: UserPreferences,
) :  BaseRepositoryImpl(gson), TransactionRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val bearerToken = { "Bearer ${prefs.getAccessToken()}" }

    override suspend fun getTransactions(
        cardId: String?,
        range: String,
    ): Result<List<Transaction>> {
        return withContext(ioDispatcher) {
            val response = api.getTransactions(
                or = "(sender_id.eq.$cardId,receiver_id.eq.$cardId)",
                authHeader = bearerToken.invoke(),
                range = range
            )

            handleApiResponse(response).fold(
                onSuccess = { trxList ->
                    val transactions: List<Transaction> = trxList.map { trx ->
                        Transaction(
                            id = trx.id,
                            senderId = trx.sender_id,
                            receiverId = trx.receiver_id,
                            amount = trx.amount,
                            createdAt = OffsetDateTime.parse(trx.created_at),
                            initiatorProfile = trx.profiles?.let {
                                Profile(
                                    id = trx.profiles.id,
                                    email = "",
                                    phone = trx.profiles.phone,
                                    fullName = trx.profiles.full_name,
                                    avatarUrl = trx.profiles.avatar_url
                                )
                            },
                            transactionType = TransactionType.fromId(trx.type) ?: TransactionType.OTHER
                        )
                    }
                    Result.success(transactions)
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
                sender_card_id = transaction.senderId,
                receiver_card_id = transaction.receiverId,
                initiator_user_id = transaction.initiatorProfile?.id
            )

            val response = api.insertTransaction(
                authHeader = bearerToken.invoke(),
                body = requestBody
            )
            handleEmptyResponse(response)
        }
    }

}
