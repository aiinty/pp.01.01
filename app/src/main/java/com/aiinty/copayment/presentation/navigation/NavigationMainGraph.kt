package com.aiinty.copayment.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.aiinty.copayment.presentation.ui.main.activity.activityScreen
import com.aiinty.copayment.presentation.ui.main.card.cardsScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardOnboardingScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardScreen
import com.aiinty.copayment.presentation.ui.main.card.createCardStyleScreen
import com.aiinty.copayment.presentation.ui.main.home.homeScreen
import com.aiinty.copayment.presentation.ui.main.profile.contactScreen
import com.aiinty.copayment.presentation.ui.main.profile.editProfileScreen
import com.aiinty.copayment.presentation.ui.main.profile.profileScreen
import com.aiinty.copayment.presentation.ui.main.qr.scanQRCodeScreen
import com.aiinty.copayment.presentation.ui.main.qr.showQRCodeScreen

fun NavGraphBuilder.mainGraph(
    modifier: Modifier = Modifier,
    navigationEventBus: NavigationEventBus
) {
    homeScreen(modifier)
    cardsScreen(modifier)
    createCardOnboardingScreen(modifier, navigationEventBus)
    createCardStyleScreen(modifier, navigationEventBus)
    createCardScreen(modifier)
    showQRCodeScreen(modifier)
    scanQRCodeScreen(modifier)
    activityScreen(modifier)
    profileScreen(modifier)
    editProfileScreen(modifier)
    contactScreen(modifier)
}
