package com.aiinty.copayment.domain.model

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
    }
}