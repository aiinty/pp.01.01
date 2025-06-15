package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.AuthResponse
import com.aiinty.copayment.data.model.EmptyResponse
import com.aiinty.copayment.data.model.RecoverRequest
import com.aiinty.copayment.data.model.RefreshTokenRequest
import com.aiinty.copayment.data.model.SignInRequest
import com.aiinty.copayment.data.model.SignUpRequest
import com.aiinty.copayment.data.model.UserMetadataResponse
import com.aiinty.copayment.data.model.UpdateUserRequest
import com.aiinty.copayment.data.model.ProfileResponse
import com.aiinty.copayment.data.model.UpdateProfileRequest
import com.aiinty.copayment.data.model.UploadImageResponse
import com.aiinty.copayment.data.model.VerifyOTPRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @POST("/storage/v1/object/avatars/{userId}/{fileName}")
    suspend fun updateAvatar(
        @Header("Authorization") authHeader: String,
        @Path("userId") userId: String,
        @Path("fileName") fileName: String,
        @Body body: RequestBody
    ): Response<UploadImageResponse>

}