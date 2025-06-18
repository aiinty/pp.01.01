package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.EmptyResponse
import com.aiinty.copayment.data.model.contact.ContactInsertRequest
import com.aiinty.copayment.data.model.contact.ContactResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ContactApi {

    @GET("/rest/v1/contacts?select=*,profiles!contacts_contact_user_id_fkey(*)")
    suspend fun getContacts(
        @Query("user_id") userId: String,
        @Header("Authorization") authHeader: String,
        @Header("Range") range: String = "0-9"
    ): Response<List<ContactResponse>>

    @POST("/rest/v1/contacts")
    suspend fun insertContact(
        @Header("Authorization") authHeader: String,
        @Body body: ContactInsertRequest
    ): Response<EmptyResponse>

}
