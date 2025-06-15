package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.EmptyResponse
import com.aiinty.copayment.data.model.profile.ProfileResponse
import com.aiinty.copayment.data.model.profile.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ProfileApi {

    @GET("/rest/v1/profiles")
    suspend fun getProfile(
        @Query("id") profileId: String,
        @Header("Authorization") authHeader: String
    ): Response<List<ProfileResponse>>

    @PATCH("/rest/v1/profiles")
    suspend fun updateProfile(
        @Query("id") profileId: String,
        @Header("Authorization") authHeader: String,
        @Body request: UpdateProfileRequest
    ): Response<EmptyResponse>

}