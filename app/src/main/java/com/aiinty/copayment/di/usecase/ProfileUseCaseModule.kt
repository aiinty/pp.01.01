package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.ContactRepository
import com.aiinty.copayment.domain.repository.ProfileRepository
import com.aiinty.copayment.domain.usecase.profile.GetCachedProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.GetContactsUseCase
import com.aiinty.copayment.domain.usecase.profile.GetProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.InsertContactUseCase
import com.aiinty.copayment.domain.usecase.profile.UpdateProfileUseCase
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
    fun provideGetContactsUseCase(
        contactsRepository: ContactRepository
    ): GetContactsUseCase {
        return GetContactsUseCase(contactsRepository)
    }

    @Provides
    fun provideInsertContactsUseCase(
        contactsRepository: ContactRepository
    ): InsertContactUseCase {
        return InsertContactUseCase(contactsRepository)
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

}
