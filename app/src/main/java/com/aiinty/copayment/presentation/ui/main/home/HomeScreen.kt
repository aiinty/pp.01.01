package com.aiinty.copayment.presentation.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.CardStyle
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.card.BaseCard
import com.aiinty.copayment.presentation.ui._components.card.BaseCardBot
import com.aiinty.copayment.presentation.ui._components.card.BaseCardTop
import com.aiinty.copayment.presentation.ui.theme.Green

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val cardMock = Card(
        id = "1",
        userId = "1",
        cardStyle = CardStyle.SPLIT,
        cardNumber = "1234567890123456",
        cardHolderName = "John Doe",
        expirationDate = "12/25",
        cvv = "123",
        balance = 10000.0
    )

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Green)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            BaseCardTop(
                modifier = Modifier.align(Alignment.BottomCenter),
                card = cardMock
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BaseCardBot(
                modifier = Modifier
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    ),
                card = cardMock,
                showBalance = true
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFF9FAFB), shape = RoundedCornerShape(16.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OperationMenuItem(Icons.Default.Refresh, "Deposit")
                OperationMenuItem(Icons.Default.Refresh, "Transfers")
                OperationMenuItem(Icons.Default.Refresh, "Withdraw")
            }

            Column(
                Modifier.fillMaxWidth(),
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Today, Mar 20",
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "All transactions",
                            color = Color(0xFF1D3A70),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color(0xFF1D3A70),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))

                Column {
                    TransactionItem("Sports", "Payment", "- $15.99", Color(0xFF1D3A70))
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                    TransactionItem("Bank of America", "Deposit", "+ $2,045.00", Color(0xFF1DAB87))
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                    TransactionItem("To Brody Armando", "Sent", "- $986.00", Color(0xFF1D3A70))

                }
            }
        }
    }
}

@Composable
fun OperationMenuItem(icon: ImageVector, label: String) {
    Column(
        Modifier
            .width(70.dp)
            .height(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(24.dp)
                .background(Color.Transparent)
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF1D3A70), modifier = Modifier.fillMaxSize())
        }
        Text(
            text = label,
            color = Color(0xFF1D3A70),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun TransactionItem(title: String, subtitle: String, amount: String, amountColor: Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .background(Color(0xFFF9FAFB), shape = RoundedCornerShape(12.dp))
            ) {

            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1D3A70))
                Text(subtitle, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = Color(0xFF6B7280))
            }
        }
        Text(amount, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = amountColor)
    }
}

fun NavGraphBuilder.homeScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.HomeScreen.route
    ){
        HomeScreen(
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        Modifier.fillMaxSize()
    )
}
