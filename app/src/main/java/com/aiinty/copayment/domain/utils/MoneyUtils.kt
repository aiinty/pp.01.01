package com.aiinty.copayment.domain.utils

object MoneyUtils {
    fun Double.formatMoney(): String = "%,.2f".format(this)
    fun Double.formatMoneyInt(): String = "%,.0f".format(this)
    fun String.toDoubleClean(): Double? = this.replace(",", "").toDoubleOrNull()
}