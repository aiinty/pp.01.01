package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.avatar.UploadImageResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AvatarApi {

    @POST("/storage/v1/object/avatars/{userId}/{fileName}")
    suspend fun updateAvatar(
        @Header("Authorization") authHeader: String,
        @Path("userId") userId: String,
        @Path("fileName") fileName: String,
        @Body body: RequestBody
    ): Response<UploadImageResponse>

}