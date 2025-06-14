package com.aiinty.copayment.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiinty.copayment.presentation.ui.auth.navigateToCreatePinCode
import com.aiinty.copayment.presentation.ui.auth.navigateToPasswordChange
import com.aiinty.copayment.presentation.ui.auth.navigateToVerifyOTP
import com.aiinty.copayment.presentation.ui.main.activityScreen
import com.aiinty.copayment.presentation.ui.main.cardsScreen
import com.aiinty.copayment.presentation.ui.main.homeScreen
import com.aiinty.copayment.presentation.ui.main.profile.contactScreen
import com.aiinty.copayment.presentation.ui.main.profile.editProfileScreen
import com.aiinty.copayment.presentation.ui.main.profile.navigateToContact
import com.aiinty.copayment.presentation.ui.main.profile.navigateToEditProfile
import com.aiinty.copayment.presentation.ui.main.profile.navigateToProfile
import com.aiinty.copayment.presentation.ui.main.profile.profileScreen
import com.aiinty.copayment.presentation.ui.main.qrCodeScreen

fun NavGraphBuilder.mainGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    homeScreen(
        modifier = modifier,
    )

    cardsScreen(
        modifier = modifier,
    )

    qrCodeScreen(
        modifier = modifier,
    )

    activityScreen(
        modifier = modifier,
    )

    profileScreen(
        modifier = modifier,
        onNavigateToContact = { navController.navigateToContact() },
        onNavigateToEditProfile = { navController.navigateToEditProfile() },
        onNavigateToChangePassword = { navController.navigateToPasswordChange(
            nextDestination = NavigationRoute.ProfileScreen.route
        ) },
        onNavigateToChangeLogInPin = { navController.navigateToCreatePinCode(
            nextDestination = NavigationRoute.ProfileScreen.route
        ) }
    )

    editProfileScreen(
        modifier = modifier,
        onNavigateToProfile = { navController.navigateToProfile(
            navOptions = {
                withPopUpTo(NavigationRoute.EditProfileScreen.route)
            }
        ) },
        onNavigateToVerify = { type, email, nextDestination ->
            navController.navigateToVerifyOTP(type, email, nextDestination)
        }
    )

    contactScreen(
        modifier = modifier,
    )
}
