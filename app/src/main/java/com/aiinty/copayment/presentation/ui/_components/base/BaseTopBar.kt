package com.aiinty.copayment.presentation.ui._components.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.TopBarState
import kotlinx.coroutines.launch

@Composable
fun BaseTopBar(
    modifier: Modifier = Modifier,
    state: TopBarState
) {
    if (!state.isVisible) return

    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier.size(40.dp)
        ) {
            if (state.showBackButton) {
                BaseIconButton(
                    onClick = {
                        scope.launch {
                            state.onBackClick()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.chevron_left),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }

        if (state.titleResId != null) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(state.titleResId),
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier.size(40.dp)
        ) {
            state.actionIcon?.let { icon ->
                IconButton(
                    onClick = {
                        scope.launch {
                            state.onActionClick()
                        }
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = state.actionIconContentDescriptionResId?.let {
                            stringResource(it)
                        }
                    )
                }
            }
        }
    }
}
