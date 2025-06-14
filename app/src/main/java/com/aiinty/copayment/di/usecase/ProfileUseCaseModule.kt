package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.AvatarRepository
import com.aiinty.copayment.domain.repository.ProfileRepository
import com.aiinty.copayment.domain.usecase.profile.GetCachedProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.GetProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.UpdateProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.UploadAvatarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProfileUseCaseModule {

    @Provides
    fun provideGetProfileUseCase(
        profileRepository: ProfileRepository
    ): GetProfileUseCase {
        return GetProfileUseCase(profileRepository)
    }

    @Provides
    fun provideGetCachedProfileUseCase(
        profileRepository: ProfileRepository
    ): GetCachedProfileUseCase {
        return GetCachedProfileUseCase(profileRepository)
    }

    @Provides
    fun provideUpdateProfileUseCase(
        profileRepository: ProfileRepository
    ): UpdateProfileUseCase {
        return UpdateProfileUseCase(profileRepository)
    }

    @Provides
    fun provideUploadAvatarUseCase(
        avatarRepository: AvatarRepository
    ): UploadAvatarUseCase {
        return UploadAvatarUseCase(avatarRepository)
    }
}
