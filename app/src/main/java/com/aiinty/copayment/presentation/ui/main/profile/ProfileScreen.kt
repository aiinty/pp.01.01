package com.aiinty.copayment.presentation.ui.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.navigation.graphs.NavigationGraph
import com.aiinty.copayment.presentation.ui._components.base.SettingItem
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.profile.ProfileAvatar
import com.aiinty.copayment.presentation.ui.main.ErrorScreen
import com.aiinty.copayment.presentation.ui.main.LoadingScreen
import com.aiinty.copayment.presentation.ui.theme.Blue
import com.aiinty.copayment.presentation.ui.theme.Orange
import com.aiinty.copayment.presentation.ui.theme.Purple
import com.aiinty.copayment.presentation.viewmodels.ProfileUiState
import com.aiinty.copayment.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    UiErrorHandler(viewModel = viewModel)
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }
    UiErrorHandler(viewModel)
    when(val state = viewModel.uiState.value) {
        is ProfileUiState.Loading -> LoadingScreen(modifier)
        is ProfileUiState.Error -> ErrorScreen(modifier)
        is ProfileUiState.Success -> ProfileScreenContent(
            modifier,
            state.profile,
            viewModel
        )
    }
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier,
    user: Profile,
    viewModel: ProfileViewModel
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                ProfileAvatar(
                    avatarUrl = user.fullAvatarUrl
                )
            }

            Text(
                text = user.fullName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D3A70)
            )

            Text(
                text = user.email,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }

        SettingItem(
            iconResId = R.drawable.contacts,
            label = stringResource(R.string.contact_list),
            onClick = { viewModel.navigateToContact() }
        )

        HorizontalDivider(thickness = 1.dp, color = Color(0xFFF3F4F6))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingItem(
                iconResId = R.drawable.settings,
                iconTintColor = Blue,
                label = stringResource(R.string.edit_profile),
                onClick = { viewModel.navigateToEditProfile() }
            )
            SettingItem(
                iconResId = R.drawable.lock,
                iconTintColor = Orange,
                label = stringResource(R.string.change_password),
                onClick = { viewModel.navigateToChangePassword() }
            )
            SettingItem(
                iconResId = R.drawable.qr_code,
                iconTintColor = Purple,
                label = stringResource(R.string.change_log_in_pin),
                onClick = { viewModel.navigateToChangeLogInPin() }
            )
        }
    }
}

fun NavGraphBuilder.profileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(
        route = NavigationRoute.ProfileScreen.route
    ){
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(NavigationGraph.MainGraph.route)
        }
        val viewModel: ProfileViewModel = hiltViewModel(parentEntry)
        ProfileScreen(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        Modifier.fillMaxSize()
    )
}
