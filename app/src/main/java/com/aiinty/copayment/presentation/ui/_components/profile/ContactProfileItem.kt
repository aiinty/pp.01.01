package com.aiinty.copayment.presentation.ui._components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.model.Profile
import com.aiinty.copayment.presentation.ui.theme.Greyscale500
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.utils.CardUtils

@Composable
fun ContactProfileItem(
    modifier: Modifier = Modifier,
    contact: Contact? = null,
    profile: Profile? = null,
    cardNumber: String
) {
    val fullName = if (contact == null && profile != null) {
        profile.fullName
    } else if (contact != null && profile == null) contact.profile.fullName else return

    val avatarUrl = if (contact == null && profile != null) {
        profile.fullAvatarUrl
    } else if (contact != null && profile == null) contact.profile.fullAvatarUrl else return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileAvatar(
            modifier = Modifier.size(48.dp),
            avatarUrl = avatarUrl,
            withBorder = false
        )

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = fullName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Greyscale900,
                letterSpacing = 0.3.sp
            )
            Text(
                text = CardUtils.formatCardNumberWithSpaces(
                    cardNumber
                ),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Greyscale500,
                letterSpacing = 0.3.sp
            )
        }
    }
}