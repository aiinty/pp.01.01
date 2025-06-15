package com.aiinty.copayment.data.repository

import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.model.profile.UpdateProfileRequest
import com.aiinty.copayment.data.network.ProfileApi
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.repository.ProfileRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val gson: Gson,
    private val prefs: UserPreferences,
) : BaseRepositoryImpl(gson), ProfileRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val bearerToken = { "Bearer ${prefs.getAccessToken()}" }

    override suspend fun getProfile(): Result<Profile> {
        return withContext(ioDispatcher) {
            try {
                val userId = prefs.getUserId()
                val userEmail = prefs.getUserEmail()
                if (userId != null && userEmail != null) {
                    val response = api.getProfile(
                        profileId = "eq.$userId",
                        authHeader = bearerToken.invoke()
                    )
                    handleApiResponse(response).fold(
                        onSuccess = {
                            val fetchedProfile = it.firstOrNull()
                            if (fetchedProfile != null) {
                                val profile = Profile(
                                    id = fetchedProfile.id,
                                    email = userEmail,
                                    phone = fetchedProfile.phone,
                                    fullName = fetchedProfile.full_name,
                                    avatarUrl = fetchedProfile.avatar_url
                                )
                                cacheProfile(profile)
                                Result.success(profile)
                            } else {
                                Result.failure(Exception())
                            }
                        },
                        onFailure = { Result.failure(it) }
                    )
                } else {
                    Result.failure(Exception())
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun cacheProfile(profile: Profile) {
        prefs.saveProfile(profile)
    }

    override suspend fun getCachedProfile(): Profile? {
        return prefs.getProfile()
    }

    override suspend fun updateProfile(profile: Profile): Result<Unit> {
        return withContext(ioDispatcher) {
            val response = api.updateProfile(
                profileId = "eq.${profile.id}",
                authHeader = bearerToken.invoke(),
                request = UpdateProfileRequest(
                    full_name = profile.fullName,
                    avatar_url = profile.avatarUrl,
                    phone = profile.phone
                )
            )
            handleEmptyResponse(response).fold(
                onSuccess = {
                    Result.success(Unit)
                },
                onFailure = {
                    Result.failure(Exception())
                }
            )
        }
    }
}