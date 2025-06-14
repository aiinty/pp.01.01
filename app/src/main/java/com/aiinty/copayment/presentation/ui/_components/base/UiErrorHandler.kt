package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.aiinty.copayment.presentation.common.UiMessage
import com.aiinty.copayment.presentation.viewmodels.BaseViewModel

@Composable
fun UiErrorHandler(
    viewModel: BaseViewModel
) {
    val showDialog = remember { mutableStateOf(false) }
    val dialogUiMessage = remember { mutableStateOf<UiMessage?>(null) }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            dialogUiMessage.value = message
            showDialog.value = true
        }
    }

    if (dialogUiMessage.value != null && showDialog.value) {
        UiMessageDialog(
            message = dialogUiMessage.value!!,
            onDismiss = {
                showDialog.value = false
                dialogUiMessage.value = null
            }
        )
    }
}