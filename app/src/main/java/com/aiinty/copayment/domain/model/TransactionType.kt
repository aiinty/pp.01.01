package com.aiinty.copayment.domain.model

import com.aiinty.copayment.R

enum class TransactionType(val id: Int, val category: TransactionCategory) {
    DEPOSIT(1, TransactionCategory.BASE),
    TRANSFER(2, TransactionCategory.BASE),
    WITHDRAW(3, TransactionCategory.BASE),
    SUBSCRIPTION(4, TransactionCategory.LIFESTYLE),
    TRAVEL(5, TransactionCategory.LIFESTYLE),
    INVESTMENT(6, TransactionCategory.FINANCE),
    OTHER(7, TransactionCategory.MISC);

    companion object {
        fun fromId(id: Int): TransactionType? = entries.find { it.id == id }
        fun toResId(transactionType: TransactionType): Int {
            return when (transactionType) {
                DEPOSIT -> R.string.deposit
                TRANSFER -> R.string.transfer
                WITHDRAW -> R.string.withdraw
                SUBSCRIPTION -> R.string.subsription
                TRAVEL -> R.string.travel
                INVESTMENT -> R.string.investment
                OTHER -> R.string.other
            }
        }
        fun isWithdrawLike(transactionType: TransactionType): Boolean {
            return when (transactionType) {
                WITHDRAW,
                SUBSCRIPTION,
                TRAVEL,
                INVESTMENT,
                OTHER -> true
                else -> false
            }
        }
        fun toIconId(transactionType: TransactionType): Int {
            return when (transactionType) {
                DEPOSIT -> R.drawable.deposit
                TRANSFER -> R.drawable.transfer
                WITHDRAW -> R.drawable.withdraw
                SUBSCRIPTION -> R.drawable.subscribe
                TRAVEL -> R.drawable.car
                INVESTMENT -> R.drawable.investments
                else -> R.drawable.withdraw
            }
        }
    }
}