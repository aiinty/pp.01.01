package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Result<Profile>
    suspend fun cacheProfile(profile: Profile)
    suspend fun getCachedProfile(): Profile?
    suspend fun updateProfile(profile: Profile): Result<Unit>
}