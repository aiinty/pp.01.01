package com.aiinty.copayment.data.network

class ApiException(val apiError: ApiError) : Exception(apiError.msg ?: apiError.message)