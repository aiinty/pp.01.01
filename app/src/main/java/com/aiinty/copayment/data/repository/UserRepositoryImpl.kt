package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.AuthResponse
import com.aiinty.copayment.data.model.EmptyResponse
import com.aiinty.copayment.data.model.RecoverRequest
import com.aiinty.copayment.data.model.SignInRequest
import com.aiinty.copayment.data.model.SignUpData
import com.aiinty.copayment.data.model.SignUpRequest
import com.aiinty.copayment.data.model.UpdateUserRequest
import com.aiinty.copayment.data.model.UserMetadataResponse
import com.aiinty.copayment.data.model.VerifyOTPRequest
import com.aiinty.copayment.data.network.ApiError
import com.aiinty.copayment.data.network.ApiErrorCode
import com.aiinty.copayment.data.network.ApiException
import com.aiinty.copayment.data.network.ApiUtils.parseApiError
import com.aiinty.copayment.data.network.SupabaseApi
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: SupabaseApi,
    private val prefs: UserPreferences,
) : UserRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun signUp(email: String, password: String, data: SignUpData?): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val response = api.signUp(request = SignUpRequest(email, password, data))
                val result = handleUserMetadataResponse(response)
                if (result.isSuccess) {
                    val metadata = result.getOrNull()
                    if (metadata != null && metadata.user_metadata == null) {
                        Result.failure(ApiException(ApiError(422, ApiErrorCode.USER_ALREADY_EXISTS.code, "User already registered")))
                    } else {
                        Result.success(Unit)
                    }
                } else {
                    result as Result<Unit>
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val response = api.signIn(
                    request = SignInRequest(email, password)
                )
                handleAuthResponse(response).fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { Result.failure(it) }
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun verifyOTP(type: OTPType, email: String, token: String): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val response = api.verifyOTP(
                    request = VerifyOTPRequest(type.code, email, token)
                )
                handleAuthResponse(response).fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { Result.failure(it) }
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun recover(email: String): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val response = api.recover(request = RecoverRequest(email))
                handleEmptyResponse(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateUser(email: String?, password: String?): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val response = api.updateUser(
                    request = UpdateUserRequest(email, password),
                    authHeader = "Bearer ${prefs.getAccessToken()}"
                )
                handleUserMetadataResponse(response).fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { Result.failure(it) }
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun <T> handleApiResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            if (response.body() != null) {
                Result.success(response.body() as T)
            } else {
                Result.success(Unit as T)
            }
        } else {
            val errorBodyString = response.errorBody()?.string()
            val apiError = parseApiError(errorBodyString)
            if (apiError != null) {
                Result.failure(ApiException(apiError))
            } else {
                Result.failure(Exception("Unknown error with code ${response.code()}"))
            }
        }
    }

    private fun handleAuthResponse(response: Response<AuthResponse>): Result<AuthResponse> {
        val result = handleApiResponse(response)
        return if (result.isSuccess) {
            val auth = result.getOrNull()
            if (auth != null){
                if (auth.user.user_metadata.email_verified) {
                    prefs.saveAccessToken(auth.access_token)
                    prefs.saveRefreshToken(auth.refresh_token)
                    prefs.saveUserId(auth.user.id)
                }
                Result.success(auth)
            } else {
                Result.failure(Exception("Empty response"))
            }
        } else {
            result
        }
    }

    private fun handleUserMetadataResponse(response: Response<UserMetadataResponse>): Result<UserMetadataResponse> {
        val result = handleApiResponse(response)
        return if (result.isSuccess) {
            val metadata = result.getOrNull()
            if (metadata != null) {
                Result.success(metadata)
            } else {
                Result.failure(Exception("Empty response"))
            }
        } else {
            result
        }
    }

    private fun handleEmptyResponse(response: Response<EmptyResponse>): Result<Unit> {
        val result = handleApiResponse(response)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            result as Result<Unit>
        }
    }
}