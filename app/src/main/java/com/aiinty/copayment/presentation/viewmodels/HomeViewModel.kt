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
import com.aiinty.copayment.domain.usecase.card.GetCardsUseCase
import com.aiinty.copayment.domain.usecase.home.GetTransactionsUseCase
import com.aiinty.copayment.domain.usecase.profile.GetProfileUseCase
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        loadUserInfo()
    }

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

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val userId = userPreferences.getUserId()
            if (userId == null) {
                _uiState.value = HomeUiState.Error
                handleError(AppException.UiResError(resId = R.string.unknown_error))
                return@launch
            }

            runCatching {
                val cards = getCardsUseCase.invoke(userId).getOrThrow()

                if (cards.isEmpty()) {
                    navigationEventBus.send(NavigationEvent.ToRoute(
                        NavigationRoute.CreateCardOnboardingScreen.route
                    ))
                    return@launch
                }

                val transactions = getTransactionsUseCase.invoke(userId, cards.first().id).getOrThrow()
                val profile = getProfileUseCase.invoke().getOrThrow()

                Triple(profile, cards, transactions)
            }.onSuccess { triple ->
                _uiState.value = HomeUiState.Success(triple)
            }.onFailure { error ->
                _uiState.value = HomeUiState.Error
                handleError(error)
            }
        }
    }

}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val userInfo: Triple<
            Profile,
            List<Card>,
            List<Transaction>
            >) : HomeUiState()
    data object Error : HomeUiState()
}
