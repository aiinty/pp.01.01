package com.aiinty.copayment.domain.usecase.profile

import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(): Result<Profile> {
        return profileRepository.getProfile()
    }
}