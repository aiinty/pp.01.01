package com.aiinty.copayment.presentation.ui._components.qr

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.utils.QRUtils.generateQrBitmap

@Composable
fun QrCodeImage(
    modifier: Modifier = Modifier,
    text: String,
    size: Int = 512
) {
    val bitmap = generateQrBitmap(text, size)
    Image(
        modifier = modifier,
        bitmap = bitmap.asImageBitmap(),
        contentDescription = stringResource(R.string.qr_code)
    )
}