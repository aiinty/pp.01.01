package com.aiinty.copayment.presentation.navigation.graphs

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationGraph(val route: String) {
    data object AuthGraph: NavigationGraph("auth_graph")
    data object MainGraph: NavigationGraph("main_graph")
}