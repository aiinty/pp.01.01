package com.aiinty.copayment.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.aiinty.copayment.presentation.common.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val errorHandler: ErrorHandler,
): ViewModel() {
    private val _errorEvent = MutableSharedFlow<UiMessage>()
    val errorEvent = _errorEvent.asSharedFlow()

    protected fun handleError(e: Throwable) {
        viewModelScope.launch {
            errorHandler.handle(
                throwable = e,
                onErrorMessage = { _errorEvent.emit(it) }
            )
        }
    }
}
