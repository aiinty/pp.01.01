package com.aiinty.copayment.presentation.ui._components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionCategory
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@Composable
fun TransactionItem(
    userProfile: Profile,
    trx: Transaction,
    isPositive: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Greyscale50, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(26.dp),
                    painter =
                        painterResource(R.drawable.transfer),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                val title = when(trx.transactionType) {
                    TransactionType.TRANSFER -> {
                        trx.initiatorProfile?.let { profile ->
                            when (profile.id) {
                                userProfile.id -> {
                                    stringResource(TransactionCategory.toResId(
                                        trx.transactionType.category
                                    ))
                                }
                                else -> {
                                    "From ${profile.fullName}"
                                }
                            }
                        } ?: stringResource(TransactionCategory.toResId(trx.transactionType.category))
                    }
                    else -> stringResource(TransactionCategory.toResId(trx.transactionType.category))
                }

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1D3A70)
                )

                Text(
                    text = stringResource(TransactionType.toResId(trx.transactionType)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
        Text(
            text = (if(isPositive) "+ " else "-") + " $${trx.amount}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (isPositive) Green else Greyscale900
        )
    }
}
