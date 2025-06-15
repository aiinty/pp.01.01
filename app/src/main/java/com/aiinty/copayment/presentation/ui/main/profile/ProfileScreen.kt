package com.aiinty.copayment.presentation.ui.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui._components.base.UiErrorHandler
import com.aiinty.copayment.presentation.ui._components.profile.ProfileAvatar
import com.aiinty.copayment.presentation.ui.theme.Blue
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.ui.theme.Orange
import com.aiinty.copayment.presentation.ui.theme.Purple
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.ProfileUiState
import com.aiinty.copayment.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState
    UiErrorHandler(viewModel = viewModel)

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    when (uiState.value) {
        is ProfileUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ProfileUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.something_went_wrong)
                )
            }
        }

        is ProfileUiState.Success -> {
            val user = (uiState.value as ProfileUiState.Success).profile

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
                        iconResId = R.drawable.password,
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
    }
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    iconResId: Int?,
    iconTintColor: Color = Green,
    label: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Greyscale50, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (iconResId != null) painterResource(iconResId) else
                        painterResource(R.drawable.settings),
                    contentDescription = label,
                    tint = iconTintColor
                )
            }

            Text(
                text = label,
                color = Greyscale900,
                style = Typography.bodyMedium,
                fontWeight = FontWeight.W500
            )
        }

        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.chevron_right),
            tint = Greyscale500,
            contentDescription = stringResource(R.string.navigate)
        )
    }
}

fun NavGraphBuilder.profileScreen(
    modifier: Modifier = Modifier,
) {
    composable(
        route = NavigationRoute.ProfileScreen.route
    ){
        ProfileScreen(
            modifier = modifier,
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
