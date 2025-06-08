package com.aiinty.copayment.presentation.navigation

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