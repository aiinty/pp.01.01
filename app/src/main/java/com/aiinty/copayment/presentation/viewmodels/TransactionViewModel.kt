package com.aiinty.copayment.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.home.InsertTransactionUseCase
import com.aiinty.copayment.domain.usecase.profile.GetCachedProfileUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val navigationEventBus: NavigationEventBus,
    private val userPreferences: UserPreferences,
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val getCachedProfileUseCase: GetCachedProfileUseCase,
    private val getCardsUseCase: GetCardsUseCase,
) : BaseViewModel(errorHandler) {
    private val _uiState = mutableStateOf<WithdrawUiState>(WithdrawUiState.Loading)
    val uiState: State<WithdrawUiState> = _uiState

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = WithdrawUiState.Loading
            runCatching {
                val userId = userPreferences.getUserId()
                if (userId == null) {
                    _uiState.value = WithdrawUiState.Error
                    handleError(AppException.UiResError(resId = R.string.unknown_error))
                    return@launch
                }
                val lastSelectedCardId = userPreferences.getSelectedCard()
                val cards = getCardsUseCase.invoke(userId).getOrThrow()

                val selectedCard: Card = if (lastSelectedCardId == null) {
                    if (cards.isEmpty()) {
                        navigationEventBus.send(
                            NavigationEvent.ToRoute(
                                NavigationRoute.CreateCardOnboardingScreen.route
                            ))
                        return@launch
                    } else {
                        userPreferences.saveSelectedCard(cards[0].id)
                        cards[0]
                    }
                } else cards.first { it.id == lastSelectedCardId }

                selectedCard
            }.onSuccess { card ->
                _uiState.value = WithdrawUiState.EnterAmount(card)
            }.onFailure { error ->
                _uiState.value = WithdrawUiState.Error
                handleError(error)
            }
        }
    }

    fun onAmountEntered(card: Card, amount: Double, transactionType: TransactionType) {
        _uiState.value = WithdrawUiState.Loading
        viewModelScope.launch {

            runCatching {
                val isWithdrawLike = TransactionType.isWithdrawLike(transactionType)
                val profile = getCachedProfileUseCase.invoke() ?: throw AppException.UiTextError("User not found")
                val transaction = Transaction(
                    id = "",
                    senderId = if (isWithdrawLike) card.id else null,
                    receiverId = if (isWithdrawLike) null else card.id,
                    amount = amount,
                    initiatorProfile = profile,
                    createdAt = OffsetDateTime.now(),
                    transactionType = transactionType
                )
                insertTransactionUseCase.invoke(transaction).getOrThrow()
            }.onSuccess {
                _uiState.value = WithdrawUiState.Success
                navigateTo(NavigationRoute.HomeScreen.route)
            }.onFailure { error ->
                _uiState.value = WithdrawUiState.Error
                handleError(error)
            }
        }
    }

}

sealed class WithdrawUiState {
    data class EnterAmount(val card: Card) : WithdrawUiState()
    data object Success : WithdrawUiState()
    data object Loading : WithdrawUiState()
    data object Error : WithdrawUiState()
}
