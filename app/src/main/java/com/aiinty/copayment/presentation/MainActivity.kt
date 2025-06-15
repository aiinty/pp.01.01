package com.aiinty.copayment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.ui.theme.CoPaymentTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var navigationEventBus: NavigationEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoPaymentTheme {
                CoPaymentApp(
                    modifier = Modifier.fillMaxSize(),
                    navigationEventBus = navigationEventBus
                )
            }
        }
    }
}
