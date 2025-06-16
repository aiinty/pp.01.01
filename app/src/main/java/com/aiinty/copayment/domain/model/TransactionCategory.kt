package com.aiinty.copayment.domain.model

enum class TransactionCategory(val id: Int){
    BASE(1),
    LIFESTYLE(2),
    FINANCE(3),
    MISC(4);

    companion object {
        fun fromId(id: Int): TransactionCategory? = entries.find { it.id == id }
    }
}