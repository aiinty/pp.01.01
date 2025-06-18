package com.aiinty.copayment.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.QRCodeInfo
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.profile.GetProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.InsertContactUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val navigationEventBus: NavigationEventBus,
    private val userPreferences: UserPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val getCardsUseCase: GetCardsUseCase,
    private val insertContactUseCase: InsertContactUseCase
): BaseViewModel(errorHandler) {
    private val _uiState = mutableStateOf<QRUiState>(QRUiState.Loading)
    val uiState: State<QRUiState> = _uiState

    init {
        loadUserInfo()
    }

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    fun navigateToSelectCard() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.SelectCardScreen.route)
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = QRUiState.Loading

            val userId = userPreferences.getUserId()
            if (userId == null) {
                _uiState.value = QRUiState.Error
                handleError(AppException.UiResError(resId = R.string.unknown_error))
                return@launch
            }
            val lastSelectedCard = userPreferences.getSelectedCard()

            runCatching {
                val cards = getCardsUseCase.invoke(userId).getOrThrow()

                if (cards.isEmpty()) {
                    navigationEventBus.send(NavigationEvent.ToRoute(
                        NavigationRoute.CreateCardOnboardingScreen.route
                    ))
                    return@launch
                }

                val foundedCard = cards.firstOrNull { it.id == lastSelectedCard }
                val selectedCard = foundedCard ?: cards[0]

                val profile = getProfileUseCase.invoke().getOrThrow()

                Pair(profile, selectedCard)
            }.onSuccess { pair ->
                _uiState.value = QRUiState.Success(
                    profile = pair.first,
                    card = pair.second
                )
            }.onFailure { error ->
                _uiState.value = QRUiState.Error
                handleError(error)
            }
        }
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch {
            runCatching {
                val contact = Contact(
                    id = "",
                    userId = contact.userId,
                    profile = contact.profile,
                    cardId = contact.cardId
                )

                if (contact.userId == contact.profile.id)
                    throw AppException.UiResError(resId = R.string.unknown_error)

                insertContactUseCase.invoke(contact)
            }.onSuccess {
                navigateTo(NavigationRoute.HomeScreen.route)
            }.onFailure {
                handleError(it)
            }
        }
    }

    fun generateQrCodeInfo(profile: Profile, card: Card): String {
        return Gson().toJson(QRCodeInfo(profile.id, card.id))
    }

    fun decodeQrContent(qrContent: String) {
        runCatching {
            Gson().fromJson(qrContent, QRCodeInfo::class.java)
        }.onSuccess {
            val userId = userPreferences.getUserId()
            if (userId == null) {
                _uiState.value = QRUiState.Error
                handleError(AppException.UiResError(resId = R.string.unknown_error))
                return
            }

            val contact = Contact(
                id = "",
                userId = userId,
                profile = Profile(
                    id = it.id,
                    fullName = "",
                    email = "",
                    avatarUrl = "",
                    phone = ""
                ),
                cardId = it.card_id
            )

            insertContact(contact)
        }.onFailure {
            handleError(it)
        }
    }
}

sealed class QRUiState {
    data object Loading : QRUiState()
    data class Success(val profile: Profile, val card: Card) : QRUiState()
    data object Error : QRUiState()
}
