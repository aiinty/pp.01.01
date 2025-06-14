package com.aiinty.copayment.domain.usecase.profile

import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.repository.ProfileRepository

class GetCachedProfileUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(): Profile? {
        return profileRepository.getCachedProfile()
    }
}