package com.aiinty.copayment.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.home.GetTransactionsUseCase
import com.aiinty.copayment.domain.usecase.profile.GetProfileUseCase
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
class HomeViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val navigationEventBus: NavigationEventBus,
    private val userPreferences: UserPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val getCardsUseCase: GetCardsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
): BaseViewModel(errorHandler) {
    private val _uiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
    val uiState: State<HomeUiState> = _uiState

    private val _selectedCard = MutableStateFlow<Card?>(null)
    val selectedCard: StateFlow<Card?> = _selectedCard

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private var currentRangeStart = 0
    private val pageSize = 10
    private var canLoadMoreInternal = true

    val canLoadMore = MutableStateFlow(true)

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    fun navigateBack() {
        viewModelScope.launch {
            navigationEventBus.send(NavigationEvent.Back)
        }
    }

    fun navigateToTransactionsHistory() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.TransactionsScreen.route)
        }
    }

    fun navigateToSelectCard() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.SelectCardScreen.route)
        }
    }

    fun navigateToTransfer() {
        viewModelScope.launch {
            navigateTo(NavigationRoute.TransferSelectScreen.route)
        }
    }

    fun navigateToTransaction(type: TransactionType) {
        viewModelScope.launch {
            navigationEventBus.send(
                NavigationEvent.ToTransaction(type)
            )
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val userId = userPreferences.getUserId()
            if (userId == null) {
                _uiState.value = HomeUiState.Error
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
                _selectedCard.value = selectedCard

                // Пагинация
                currentRangeStart = 0
                _transactions.value = emptyList()
                canLoadMoreInternal = true
                canLoadMore.value = true
                loadNextTransactions(selectedCard.id)

                val profile = getProfileUseCase.invoke().getOrThrow()

                Pair(profile, cards)
            }.onSuccess { (profile, cards) ->
                _uiState.value = HomeUiState.Success(
                    profile,
                    cards,
                    _transactions.value
                )
            }.onFailure { error ->
                _uiState.value = HomeUiState.Error
                handleError(error)
            }
        }
    }

    fun selectCard(card: Card) {
        viewModelScope.launch {
            userPreferences.saveSelectedCard(card.id)
            _selectedCard.value = card
            loadUserInfo()
            navigationEventBus.send(
                NavigationEvent.Back
            )
        }
    }

    fun loadNextTransactions(cardId: String?) {
        viewModelScope.launch {
            if (!canLoadMoreInternal) return@launch
            val range = "${currentRangeStart}-${currentRangeStart + pageSize - 1}"
            val result = getTransactionsUseCase.invoke(cardId, range)
            result.fold(
                onSuccess = { newItems ->
                    _transactions.value = _transactions.value + newItems
                    canLoadMoreInternal = newItems.size == pageSize
                    canLoadMore.value = canLoadMoreInternal
                    currentRangeStart += newItems.size
                },
                onFailure = {
                    handleError(it)
                    canLoadMoreInternal = false
                    canLoadMore.value = false
                }
            )
        }
    }

}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(
        val profile: Profile,
        val cards: List<Card>,
        val transactions: List<Transaction>
    ) : HomeUiState()
    data object Error : HomeUiState()
}
