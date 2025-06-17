import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.presentation.ui._components.home.TransactionItem
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.utils.TransactionsUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun GroupedTransactionsList(
    cardTransactions: List<Transaction>,
    profile: Profile,
    selectedCard: Card
) {
    if (cardTransactions.isEmpty()) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.now_there_are_no_transactions),
                color = Greyscale500,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
        return
    }

    val grouped = cardTransactions.groupBy { it.createdAt.toLocalDate() }

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d")

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        grouped.toSortedMap(compareByDescending { it }).forEach { (date, transactions) ->
            item {
                val prefix = when (date) {
                    today -> stringResource(R.string.today)
                    yesterday -> stringResource(R.string.yesterday)
                    else -> null
                }
                val label = if (prefix != null) "$prefix, ${date.format(dateFormatter)}" else
                    date.format(dateFormatter)

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = label,
                    color = Greyscale500,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }

            items(transactions.sortedByDescending { it.createdAt }) { item ->
                TransactionItem(
                    userProfile = profile,
                    trx = item,
                    isPositive = TransactionsUtils.isPositiveTransaction(selectedCard, item)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color(0xFFF3F4F6)
                )
            }
        }
    }
}
