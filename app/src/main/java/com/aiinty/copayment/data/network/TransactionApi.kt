package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.transaction.TransactionInsertRequest
import com.aiinty.copayment.data.model.transaction.TransactionInsertResponse
import com.aiinty.copayment.data.model.transaction.TransactionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionApi {

    @GET("/rest/v1/transactions?select=*,profiles(*)")
    suspend fun getTransactions(
        @Query("or") or: String,
        @Header("Authorization") authHeader: String,
        @Header("Range") range: String = "0-9"
    ): Response<List<TransactionsResponse>>

    @POST("/rest/v1/transactions")
    suspend fun insertTransaction(
        @Header("Authorization") authHeader: String,
        @Body body: TransactionInsertRequest
    ): Response<TransactionInsertResponse>

}
