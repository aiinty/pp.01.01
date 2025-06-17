package com.aiinty.copayment.domain.model

import com.aiinty.copayment.R

enum class TransactionCategory(val id: Int){
    BASE(1),
    LIFESTYLE(2),
    FINANCE(3),
    MISC(4);

    companion object {
        fun fromId(id: Int): TransactionCategory? = entries.find { it.id == id }
        fun toResId(transactionCategory: TransactionCategory): Int {
            return when (transactionCategory) {
                BASE -> R.string.base
                LIFESTYLE -> R.string.lifestyle
                FINANCE -> R.string.finance
                MISC -> R.string.misc
            }
        }
    }
}