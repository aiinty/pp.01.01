package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.card.CardInsertRequest
import com.aiinty.copayment.data.model.card.GetCardsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CardApi {

    @GET("/rest/v1/cards?select=*")
    suspend fun getCards(
        @Query("user_id") userId: String,
        @Header("Authorization") authHeader: String,
    ): Response<List<GetCardsResponse>>

    @POST("/rest/v1/cards")
    suspend fun insertCard(
        @Header("Authorization") authHeader: String,
        @Body body: CardInsertRequest
    ): Response<Unit>

}
