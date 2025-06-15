package com.aiinty.copayment.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.usecase.auth.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.profile.GetCachedProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.GetProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.UpdateProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.UploadAvatarUseCase
import com.aiinty.copayment.domain.utils.FileUtils
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val navigationEventBus: NavigationEventBus,
    private val userPreferences: UserPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val getCachedProfileUseCase: GetCachedProfileUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase
): BaseViewModel(errorHandler) {
    private val _uiState = mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
    val uiState: State<ProfileUiState> = _uiState

    init {
        loadUser()
    }

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    private suspend fun navigateToProfileInternal() {
        navigateTo(NavigationRoute.ProfileScreen.route)
    }

    private fun navigateToVerifyOTP(type: OTPType, email: String, nextDestination: String?) {
        viewModelScope.launch {
            navigateTo(
                NavigationRoute.VerifyOTPScreen(
                    type = type,
                    email = email,
                    nextDestination = nextDestination
                ).route
            )
        }
    }

    fun navigateToContact() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.ContactScreen.route)
        }
    }

    fun navigateToEditProfile() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.EditProfileScreen.route)
        }
    }

    fun navigateToChangePassword() {
        viewModelScope.launch {
            navigateTo(
                NavigationRoute.PasswordChangeScreen(
                    nextDestination = NavigationRoute.ProfileScreen.route
                ).route
            )
        }
    }

    fun navigateToChangeLogInPin() {
        viewModelScope.launch {
            navigateTo(
                NavigationRoute.CreatePinCodeScreen(
                    nextDestination = NavigationRoute.ProfileScreen.route
                ).route
            )
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            val cached = getCachedProfileUseCase.invoke()
            if (cached != null) {
                _uiState.value = ProfileUiState.Success(cached)
            }

            val result = getProfileUseCase.invoke()
            _uiState.value = result.fold(
                onSuccess = { ProfileUiState.Success(it) },
                onFailure = {
                    if (cached == null) {
                        handleError(it)
                        ProfileUiState.Error
                    } else {
                        _uiState.value
                    }
                }
            )
        }
    }

    fun updateProfile(profile: Profile, newAvatarFile: File?) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            var updatedProfile = profile

            if (newAvatarFile != null) {
                val newAvatarFileName = FileUtils.generateTimeStampFileName(".png")
                val avatarResult = uploadAvatarUseCase.invoke(
                    userId = profile.id,
                    fileName = newAvatarFileName,
                    file = newAvatarFile
                )

                if (avatarResult.isFailure) {
                    val errorMessage = "Error uploading avatar"
                    handleError(avatarResult.exceptionOrNull() ?: AppException.UiTextError(errorMessage))
                    _uiState.value = ProfileUiState.Error
                    return@launch
                }

                updatedProfile = profile.copy(
                    avatarUrl = "${profile.id}/${newAvatarFileName}"
                )
            }

            val profileResult = updateProfileUseCase.invoke(updatedProfile)

            if (profileResult.isFailure) {
                val errorMessage = "Profile update failed."
                handleError(profileResult.exceptionOrNull() ?: AppException.UiTextError(errorMessage))
                _uiState.value = ProfileUiState.Error
                return@launch
            }

            val cached = getCachedProfileUseCase.invoke()
            var userResult: Result<Unit> = Result.success(Unit)

            if (cached != null && profile.email != cached.email) {
                userResult = updateUserUseCase.invoke(email = profile.email, password = null)
                if (userResult.isFailure) {
                    val errorMessage = "Email update failed."
                    handleError(userResult.exceptionOrNull() ?: AppException.UiTextError(errorMessage))
                    _uiState.value = ProfileUiState.Error
                    return@launch
                }
                // Saving in preferences because there is no endpoint for resending the email confirmation
                userPreferences.saveUserNewEmail(email = profile.email)

                navigateToVerifyOTP(
                    OTPType.EMAIL_CHANGE,
                    cached.email,
                    NavigationRoute.VerifyOTPScreen(
                        type = OTPType.EMAIL_CHANGE,
                        email = profile.email,
                        nextDestination = NavigationRoute.ProfileScreen.route
                    ).route
                )
            } else {
                navigateToProfileInternal()
            }

            loadUser()
        }
    }
}

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val profile: Profile) : ProfileUiState()
    data object Error : ProfileUiState()
}
