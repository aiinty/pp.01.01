package com.aiinty.copayment.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.R
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.domain.model.AppException
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.home.InsertTransactionUseCase
import com.aiinty.copayment.domain.usecase.profile.GetCachedProfileUseCase
import com.aiinty.copayment.domain.usecase.profile.GetContactsUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val navigationEventBus: NavigationEventBus,
    private val userPreferences: UserPreferences,
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val getCachedProfileUseCase: GetCachedProfileUseCase,
    private val getCardsUseCase: GetCardsUseCase,
    private val getContactsUseCase: GetContactsUseCase
) : BaseViewModel(errorHandler) {
    private val _uiState = mutableStateOf<TransferUiState>(TransferUiState.Loading)
    val uiState: State<TransferUiState> = _uiState

    private val _selectedCard = MutableStateFlow<Card?>(null)
    val selectedCard: StateFlow<Card?> = _selectedCard

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

    private suspend fun navigateTo(route: String) {
        navigationEventBus.send(NavigationEvent.ToRoute(route))
    }

    fun selectCard(card: Card) {
        _selectedCard.value = card
    }

    fun selectContact(contact: Contact) {
        _selectedContact.value = contact
    }

    fun navigateToHome() {
        viewModelScope.launch {
            navigationEventBus.send(NavigationEvent.ToRoute(NavigationRoute.HomeScreen.route))
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = TransferUiState.Loading

            val userId = userPreferences.getUserId()
            if (userId == null) {
                _uiState.value = TransferUiState.Error
                handleError(AppException.UiResError(resId = R.string.unknown_error))
                return@launch
            }

            runCatching {
                val cards = getCardsUseCase.invoke(userId).getOrThrow()

                if (cards.isEmpty()) {
                    navigationEventBus.send(
                        NavigationEvent.ToRoute(
                            NavigationRoute.CreateCardOnboardingScreen.route
                        ))
                    return@launch
                }
                val contacts = getContactsUseCase.invoke(userId).getOrThrow()
                Pair(cards, contacts)
            }.onSuccess { (cards, contacts) ->
                _uiState.value = TransferUiState.SelectCardAndContact(
                    cards = cards,
                    contacts = contacts
                )
            }.onFailure { error ->
                _uiState.value = TransferUiState.Error
                handleError(error)
            }
        }
    }

    fun onCardAndContactSelected(card: Card, contact: Contact) {
        _selectedCard.value = card
        _selectedContact.value = contact
        _uiState.value = TransferUiState.EnterAmount(card, contact)
        viewModelScope.launch {
            navigateTo(NavigationRoute.TransferAmountScreen.route)
        }
    }

    fun onAmountEntered(amount: Double) {
        val card = selectedCard.value
        val contact = selectedContact.value
        if (card == null || contact == null) {
            _uiState.value = TransferUiState.Error
            return
        }
        _uiState.value = TransferUiState.Loading
        viewModelScope.launch {

            runCatching {
                val profile = getCachedProfileUseCase.invoke()
                    ?: throw AppException.UiResError(R.string.unknown_error)
                val transaction = Transaction(
                    id = "",
                    senderId = card.id,
                    receiverId = contact.cardId,
                    amount = amount,
                    initiatorProfile = profile,
                    createdAt = OffsetDateTime.now(),
                    transactionType = TransactionType.TRANSFER
                )
               insertTransactionUseCase.invoke(transaction).getOrThrow()
            }.onSuccess {
                _uiState.value = TransferUiState.Success(amount)
                navigateTo(NavigationRoute.TransferProofScreen.route)
            }.onFailure { error ->
                _uiState.value = TransferUiState.Error
                handleError(error)
            }
        }
    }
}

sealed class TransferUiState {
    data class SelectCardAndContact(val cards: List<Card>, val contacts: List<Contact>) : TransferUiState()
    data class EnterAmount(val card: Card, val contact: Contact) : TransferUiState()
    data class Success(val amount: Double) : TransferUiState()
    data object Loading : TransferUiState()
    data object Error : TransferUiState()
}
