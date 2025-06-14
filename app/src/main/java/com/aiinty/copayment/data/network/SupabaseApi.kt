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

interface SupabaseApi {

    @POST("/auth/v1/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<UserMetadataResponse>

    @POST("/auth/v1/token")
    suspend fun signIn(
        @Query("grant_type") grantType: String = "password",
        @Body request: SignInRequest
    ): Response<AuthResponse>

    @POST("/auth/v1/token")
    suspend fun refreshToken(
        @Query("grant_type") grantType: String = "refresh_token",
        @Body request: RefreshTokenRequest
    ): Response<AuthResponse>

    @POST("/auth/v1/verify")
    suspend fun verifyOTP(
        @Body request: VerifyOTPRequest
    ): Response<AuthResponse>

    @POST("/auth/v1/recover")
    suspend fun recover(
        @Body request: RecoverRequest
    ): Response<EmptyResponse>

    @PUT("/auth/v1/user")
    suspend fun updateUser(
        @Header("Authorization") authHeader: String,
        @Body request: UpdateUserRequest
    ): Response<UserMetadataResponse>

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