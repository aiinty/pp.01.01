package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.AvatarRepository
import com.aiinty.copayment.domain.usecase.profile.UploadAvatarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AvatarUseCaseModule {

    @Provides
    fun provideUploadAvatarUseCase(
        avatarRepository: AvatarRepository
    ): UploadAvatarUseCase {
        return UploadAvatarUseCase(avatarRepository)
    }

}
