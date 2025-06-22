package com.aiinty.copayment.presentation.ui._components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.TransactionCategory
import com.aiinty.copayment.domain.model.TransactionType
import com.aiinty.copayment.presentation.ui._components.base.BaseFilterChip

@JvmOverloads
@Composable
fun TransactionsFilterRow(
    modifier: Modifier = Modifier,
    selectedCategories: Set<TransactionCategory>,
    onCategoryToggle: (TransactionCategory) -> Unit,
    onCategoryClear: () -> Unit,
    selectedTypes: Set<TransactionType>,
    onTypeToggle: (TransactionType) -> Unit,
    onTypeClear: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            item {
                BaseFilterChip (
                    selected = selectedCategories.isEmpty(),
                    onClick = onCategoryClear,
                    label = { Text(stringResource(R.string.all)) }
                )
            }

            items(TransactionCategory.entries.size) { idx ->
                val category = TransactionCategory.entries[idx]
                BaseFilterChip(
                    selected = selectedCategories.contains(category),
                    onClick = { onCategoryToggle(category) },
                    label = { Text(stringResource(TransactionCategory.toResId(category))) }
                )
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            item {
                BaseFilterChip(
                    selected = selectedTypes.isEmpty(),
                    onClick = onTypeClear,
                    label = { Text(stringResource(R.string.all)) }
                )
            }

            items(TransactionType.entries.size) { idx ->
                val type = TransactionType.entries[idx]
                BaseFilterChip(
                    selected = selectedTypes.contains(type),
                    onClick = { onTypeToggle(type) },
                    label = { Text(stringResource(TransactionType.toResId(type))) }
                )
            }
        }
    }
}
