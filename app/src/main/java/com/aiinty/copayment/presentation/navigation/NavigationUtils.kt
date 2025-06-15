package com.aiinty.copayment.presentation.navigation

import androidx.navigation.NavOptionsBuilder

object NavigationUtils {
    fun buildRoute(
        routeBase: String,
        params: Map<String, String?> = emptyMap()
    ): String {
        val nonNullParams = params.filterValues { it != null }
        if (nonNullParams.isEmpty()) {
            return routeBase
        }
        val queryString = nonNullParams.entries.joinToString("&") { "${it.key}=${it.value}" }
        return "$routeBase?$queryString"
    }
}

fun NavOptionsBuilder.withPopUpTo(
    route: String,
    isInclusive: Boolean = true,
    isLaunchSingleTop: Boolean = true
) {
    popUpTo(route) { this.inclusive = isInclusive }
    launchSingleTop = isLaunchSingleTop
}

