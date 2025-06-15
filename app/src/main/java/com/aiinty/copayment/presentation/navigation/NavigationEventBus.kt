package com.aiinty.copayment.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NavigationEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

    suspend fun send(event: NavigationEvent) {
        _events.emit(event)
    }
}