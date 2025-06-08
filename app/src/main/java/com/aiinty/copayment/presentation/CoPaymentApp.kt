package com.aiinty.copayment.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.aiinty.copayment.presentation.navigation.CoPaymentNavHost

@Composable
fun CoPaymentApp(
    modifier: Modifier = Modifier
){
    Scaffold(
        modifier = modifier,
        containerColor = Color.White
    ) { innerPadding ->
        CoPaymentNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CoPaymentApp(
        modifier = Modifier.fillMaxSize()
    )
}
