package com.aiinty.copayment.presentation.ui._components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.ui.theme.Greyscale50

@Composable
fun OperationMenuRow(
    modifier: Modifier = Modifier,
    onDepositClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Greyscale50, shape = RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OperationMenuItem(
            modifier = Modifier.weight(1f),
            iconResId = R.drawable.deposit,
            labelResId = R.string.deposit,
            onClick = onDepositClick
        )
        OperationMenuItem(
            modifier = Modifier.weight(1f),
            iconResId = R.drawable.transfer,
            labelResId = R.string.transfers,
            onClick = onTransferClick
        )
        OperationMenuItem(
            modifier = Modifier.weight(1f),
            iconResId = R.drawable.withdraw,
            labelResId = R.string.withdraw,
            onClick = onWithdrawClick
        )
        OperationMenuItem(
            modifier = Modifier.weight(1f),
            iconResId = R.drawable.more,
            labelResId = R.string.more,
            onClick = onMoreClick
        )
    }
}
