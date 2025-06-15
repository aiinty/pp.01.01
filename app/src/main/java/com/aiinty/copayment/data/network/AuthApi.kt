package com.aiinty.copayment.data.network

import com.aiinty.copayment.data.model.auth.AuthResponse
import com.aiinty.copayment.data.model.EmptyResponse
import com.aiinty.copayment.data.model.auth.RecoverRequest
import com.aiinty.copayment.data.model.auth.RefreshTokenRequest
import com.aiinty.copayment.data.model.auth.SignInRequest
import com.aiinty.copayment.data.model.auth.SignUpRequest
import com.aiinty.copayment.data.model.auth.UserMetadataResponse
import com.aiinty.copayment.data.model.auth.UpdateUserRequest
import com.aiinty.copayment.data.model.auth.VerifyOTPRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthApi {

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

}