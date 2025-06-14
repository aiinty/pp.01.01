package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.network.ApiError
import com.aiinty.copayment.data.network.ApiException
import com.google.gson.Gson
import retrofit2.Response

abstract class BaseRepositoryImpl(
    private val gson: Gson
) {
    protected fun <T> handleApiResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(Exception("Empty response body"))
        } else {
            val errorBody = response.errorBody()?.string()
            val apiError = parseApiError(errorBody)
            if (apiError != null) Result.failure(ApiException(apiError))
            else Result.failure(Exception("Unknown error with code ${response.code()}"))
        }
    }

    protected fun handleEmptyResponse(response: Response<*>): Result<Unit> {
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            val errorBody = response.errorBody()?.string()
            val apiError = parseApiError(errorBody)
            if (apiError != null) Result.failure(ApiException(apiError))
            else Result.failure(Exception("Unknown error with code ${response.code()}"))
        }
    }

    private fun parseApiError(errorBody: String?): ApiError? {
        return try {
            gson.fromJson(errorBody, ApiError::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
