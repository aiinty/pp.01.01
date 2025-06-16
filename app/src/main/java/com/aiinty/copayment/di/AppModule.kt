package com.aiinty.copayment.di

import android.content.Context
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.network.AuthApi
import com.aiinty.copayment.data.network.AvatarApi
import com.aiinty.copayment.data.network.CardApi
import com.aiinty.copayment.data.network.ProfileApi
import com.aiinty.copayment.data.network.RetrofitInstance
import com.aiinty.copayment.data.network.TransactionApi
import com.aiinty.copayment.data.repository.AvatarRepositoryImpl
import com.aiinty.copayment.data.repository.CardRepositoryImpl
import com.aiinty.copayment.data.repository.ProfileRepositoryImpl
import com.aiinty.copayment.data.repository.TransactionRepositoryImpl
import com.aiinty.copayment.data.repository.UserRepositoryImpl
import com.aiinty.copayment.domain.repository.AvatarRepository
import com.aiinty.copayment.domain.repository.CardRepository
import com.aiinty.copayment.domain.repository.ProfileRepository
import com.aiinty.copayment.domain.repository.TransactionRepository
import com.aiinty.copayment.domain.repository.UserRepository
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNavigationEventBus(): NavigationEventBus = NavigationEventBus()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideAuthApi(): AuthApi = RetrofitInstance.authApi

    @Provides
    fun provideTransactionApi(): TransactionApi = RetrofitInstance.transactionApi

    @Provides
    fun provideCardApi(): CardApi = RetrofitInstance.cardApi

    @Provides
    fun provideProfileApi(): ProfileApi = RetrofitInstance.profileApi

    @Provides
    fun provideAvatarApi(): AvatarApi = RetrofitInstance.avatarApi

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences =
        UserPreferences(context)

    @Provides
    fun provideErrorHandler(
        navigationEventBus: NavigationEventBus,
        userPrefs: UserPreferences
    ): ErrorHandler = ErrorHandler(userPrefs, navigationEventBus)

    @Provides
    @Singleton
    fun provideUserRepository(
        api: AuthApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): UserRepository = UserRepositoryImpl(api, gson, userPrefs)

    @Provides
    @Singleton
    fun provideTransactionRepository(
        api: TransactionApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): TransactionRepository = TransactionRepositoryImpl(api, gson, userPrefs)

    @Provides
    @Singleton
    fun provideProfileRepository(
        api: ProfileApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): ProfileRepository = ProfileRepositoryImpl(api, gson, userPrefs)

    @Provides
    @Singleton
    fun provideAvatarRepository(
        api: AvatarApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): AvatarRepository = AvatarRepositoryImpl(api, gson, userPrefs)

    @Provides
    @Singleton
    fun provideCardRepository(
        api: CardApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): CardRepository = CardRepositoryImpl(api, gson, userPrefs)

}