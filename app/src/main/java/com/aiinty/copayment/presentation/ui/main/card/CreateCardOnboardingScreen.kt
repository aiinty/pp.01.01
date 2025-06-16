package com.aiinty.copayment.presentation.ui.main.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.NavigationEvent
import com.aiinty.copayment.presentation.navigation.NavigationEventBus
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.base.BaseButton
import com.aiinty.copayment.presentation.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun CreateCardOnboardingScreen(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.create_card_onboarding),
            contentDescription = stringResource(R.string.onboarding_illustration)
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.create_card_onboarding_title),
                textAlign = TextAlign.Start,
                style = Typography.titleMedium,
                fontSize = 32.sp,
            )

            Text(
                text = stringResource(R.string.create_card_onboarding_desc),
                textAlign = TextAlign.Start,
                style = Typography.bodyMedium,
            )
        }


        BaseButton(
            onClick = {
                scope.launch {
                    navigationEventBus.send(
                        NavigationEvent.ToRoute(
                            NavigationRoute.CreateCardStyleScreen.route
                        )
                    )
                }
            }
        ) {
            Text(
                text = stringResource(R.string.get_free_card),
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                color = Color.White
            )
        }
    }
}

fun NavGraphBuilder.createCardOnboardingScreen(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus,
) {
    composable(
        route = NavigationRoute.CreateCardOnboardingScreen.route
    ){
        CreateCardOnboardingScreen(
            modifier = modifier,
            navigationEventBus = navigationEventBus,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateCardOnboardingScreenPreview() {
    CreateCardOnboardingScreen(
        Modifier.fillMaxSize(),
        NavigationEventBus()
    )
}
