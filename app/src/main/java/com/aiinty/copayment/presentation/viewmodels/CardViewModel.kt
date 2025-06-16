package com.aiinty.copayment.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.card.InsertCardUseCase
import com.aiinty.copayment.domain.usecase.card.UpdateCardUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val navigationEventBus: NavigationEventBus,
    private val userPreferences: UserPreferences,
    private val getCardsUseCase: GetCardsUseCase,
    private val insertCardUseCase: InsertCardUseCase,
    private val updateCardUseCase: UpdateCardUseCase
): BaseViewModel(errorHandler) {
    private val _uiState = mutableStateOf<CardUiState>(CardUiState.Loading)
    val uiState: State<CardUiState> = _uiState

    private val _selectedCard = MutableStateFlow<Card?>(null)
    val selectedCard: StateFlow<Card?> = _selectedCard

    init {
        loadCards()
    }

    fun selectCard(card: Card) {
        _selectedCard.value = card
    }

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    fun navigateToHome() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.HomeScreen.route)
        }
    }

    fun navigateToCreateStyleCard() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.CreateCardStyleScreen.route)
        }
    }

    fun navigateToEditCard() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.EditCardScreen.route)
        }
    }

    fun selectedCardIsNull() {
        viewModelScope.launch {
            handleError(AppException.UiResError(resId = R.string.unknown_error))
            navigateTo(NavigationRoute.CardsScreen.route)
        }
    }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading

            try {
                val userId = userPreferences.getUserId()
                if (userId != null) {
                    val result = getCardsUseCase.invoke(userId)
                    result.fold(
                        onSuccess = {
                            _uiState.value = CardUiState.Success(it)
                        },
                        onFailure = {
                            _uiState.value = CardUiState.Error
                            handleError(it)
                        }
                    )
                } else {
                    _uiState.value = CardUiState.Error
                    handleError(AppException.UiResError(resId = R.string.unknown_error))
                }
            } catch (e: AppException) {
                _uiState.value = CardUiState.Error
                handleError(e)
            }
        }
    }

    fun insertCard(card: Card) {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading

            try {
                val userId = userPreferences.getUserId()
                if (userId != null) {
                    val cardWithUserId = card.copy(userId = userId)

                    val result = insertCardUseCase.invoke(cardWithUserId)
                    result.fold(
                        onSuccess = {
                            navigateToHome()
                        },
                        onFailure = {
                            _uiState.value = CardUiState.Error
                            handleError(it)
                        }
                    )
                } else {
                    _uiState.value = CardUiState.Error
                    handleError(AppException.UiResError(resId = R.string.unknown_error))
                }
            } catch (e: AppException) {
                _uiState.value = CardUiState.Error
                handleError(e)
            }
        }
    }

    fun updateCard(card: Card) {
        viewModelScope.launch {
            _uiState.value = CardUiState.Loading

            try {
                val result = updateCardUseCase.invoke(card)
                result.fold(
                    onSuccess = {
                        navigateTo(NavigationRoute.CardsScreen.route)
                    },
                    onFailure = {
                        _uiState.value = CardUiState.Error
                        handleError(it)
                    }
                )
            } catch (e: AppException) {
                _uiState.value = CardUiState.Error
                handleError(e)
            }
        }
    }

}

sealed class CardUiState {
    data object Loading : CardUiState()
    data class Success(val cards: List<Card>) : CardUiState()
    data object Error : CardUiState()
}
