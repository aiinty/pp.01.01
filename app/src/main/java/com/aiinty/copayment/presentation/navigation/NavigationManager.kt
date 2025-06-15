package com.aiinty.copayment.presentation.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed class NavigationEvent {
    data class Navigate(val route: String) : NavigationEvent()
    data class NavigateUp(val result: Any? = null) : NavigationEvent()
    data object BlockNavigation : NavigationEvent()
    data object UnblockNavigation : NavigationEvent()
}

class NavigationManager {
    private val _events = Channel<NavigationEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    suspend fun navigate(route: String) {
        _events.send(NavigationEvent.Navigate(route))
    }

    suspend fun navigateUp() {
        _events.send(NavigationEvent.NavigateUp())
    }

    suspend fun block() {
        _events.send(NavigationEvent.BlockNavigation)
    }

    suspend fun unblock() {
        _events.send(NavigationEvent.UnblockNavigation)
    }
}