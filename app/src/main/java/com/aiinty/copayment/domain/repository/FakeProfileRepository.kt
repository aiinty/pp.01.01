package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Profile

class FakeProfileRepository : ProfileRepository {

    private var cachedProfile: Profile? = null

    override suspend fun getProfile(): Result<Profile> {

        val profile = Profile(
            id = "12345",
            fullName = "Test User",
            phone = "88005553535",
            email = "test@example.com",
            avatarUrl = "12345/avatar.png"
        )
        cachedProfile = profile
        return Result.success(profile)
    }

    override suspend fun cacheProfile(profile: Profile) {
        cachedProfile = profile
    }

    override suspend fun getCachedProfile(): Profile? {
        return cachedProfile
    }

    override suspend fun updateProfile(profile: Profile): Result<Unit> {
        cachedProfile = profile
        return Result.success(Unit)
    }
}
