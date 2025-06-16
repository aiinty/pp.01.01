package com.aiinty.copayment.presentation.navigation.graphs

import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.presentation.ui.main.card.EditCardScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationGraph(val route: String) {
    data object AuthGraph: NavigationGraph("auth_graph")
    data object MainGraph: NavigationGraph("main_graph")
    data object CardGraph: NavigationGraph("card_graph")
}