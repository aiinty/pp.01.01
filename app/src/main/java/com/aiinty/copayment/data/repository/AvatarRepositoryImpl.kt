package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.network.SupabaseApi
import com.aiinty.copayment.domain.repository.AvatarRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AvatarRepositoryImpl @Inject constructor(
    private val api: SupabaseApi,
    private val gson: Gson,
    private val prefs: UserPreferences,
) :  BaseRepositoryImpl(gson), AvatarRepository {
    private val bearerToken = { "Bearer ${prefs.getAccessToken()}" }

    override suspend fun uploadAvatar(
        userId: String,
        fileName: String,
        file: File
    ): Result<Unit> {
        val imageBytes = file.readBytes()
        val requestBody = imageBytes.toRequestBody("image/png".toMediaType())

        val response = api.updateAvatar(
            authHeader = bearerToken.invoke(),
            userId = userId,
            fileName = fileName,
            body = requestBody
        )

        return handleEmptyResponse(response)
    }
}