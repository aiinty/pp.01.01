package com.aiinty.copayment.domain.usecase.profile

import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(profile: Profile): Result<Unit> {
        return profileRepository.updateProfile(profile)
    }
}