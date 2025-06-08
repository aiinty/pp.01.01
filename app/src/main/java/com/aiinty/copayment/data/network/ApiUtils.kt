package com.aiinty.copayment.data.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object ApiUtils {
    fun parseApiError(errorBody: String?): ApiError? {
        if (errorBody == null) return null
        return try {
            Gson().fromJson(errorBody, ApiError::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}