package com.aiinty.copayment.presentation.ui._components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.presentation.ui._components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale100
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeSheet(
    onSelect: (TransactionType?) -> Unit,
    onDismiss: () -> Unit
) {
    val types = listOf(
        TransactionType.SUBSCRIPTION,
        TransactionType.TRAVEL,
        TransactionType.INVESTMENT,
        TransactionType.OTHER,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        tonalElevation = 8.dp,
        dragHandle = { Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Greyscale100)
            )
        } }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.select_transaction_type),
                color = Greyscale900,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(8.dp))
            types.forEach { type ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelect(type) },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Greyscale50
                    )
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(TransactionType.toIconId(type)),
                            contentDescription = null,
                            tint = Green,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = stringResource(TransactionType.toResId(type)),
                            color = Greyscale900,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            BaseTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = { onDismiss() },
                text = stringResource(R.string.cancel),
                enabledColor = Green
            )
        }
    }
}