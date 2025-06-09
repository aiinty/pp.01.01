package com.aiinty.copayment.presentation.ui.components.base

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.model.UiMessage
import com.aiinty.copayment.presentation.ui.theme.Typography

@Composable
fun UiMessageDialog(
    message: UiMessage,
    onDismiss: () -> Unit = { }
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(
            text = stringResource(R.string.error),
            style = Typography.titleMedium
        ) },
        text = { Text(
            text = when(message) {
                is UiMessage.StringRes -> stringResource(message.resId)
                is UiMessage.Text -> message.message
            },
            style = Typography.bodyMedium
        ) },
        confirmButton = {
            BaseButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorDialogPreview() {
    UiMessageDialog(
        message = UiMessage.Text("Example")
    )
}