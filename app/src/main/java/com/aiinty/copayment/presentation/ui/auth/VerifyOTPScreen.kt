package com.aiinty.copayment.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aiinty.copayment.R
import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.utils.EmailUtils
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.base.BaseButton
import com.aiinty.copayment.presentation.ui.components.base.BaseIconButton
import com.aiinty.copayment.presentation.ui.components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui.theme.Green
import com.aiinty.copayment.presentation.ui.theme.Greyscale50
import com.aiinty.copayment.presentation.ui.theme.Greyscale900
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.aiinty.copayment.presentation.viewmodels.AuthViewModel

@Composable
fun VerifyOTPScreen(
    modifier: Modifier = Modifier,
    type: OTPType,
    email: String,
    nextDestination: String? = null,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit = {},
    onNavigateToNext: (String) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { target ->
            when(target) {
                is NavigationRoute.HomeScreen -> {
                    if (nextDestination != null) {
                        onNavigateToNext(nextDestination)
                    }
                }
                else -> {}
            }
        }
    }

    var token = remember { mutableStateOf("") }
    val cooldown = viewModel.resendCooldownSeconds.collectAsState()
    val canResend = viewModel.canResend.collectAsState()
    val isValidatedInputs = token.value.length == 6

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            BaseIconButton(
                onClick = onNavigateToBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left),
                    contentDescription = stringResource(R.string.back)
                )
            }

            VerifyOTPHeader(email = email)

            VerifyOTPFields(
                modifier = Modifier.fillMaxWidth(),
                token = token
            )

            BaseTextButton(
                text = if (cooldown.value == 0)
                    stringResource(R.string.verify_otp_resend_code) else
                        stringResource(R.string.verify_otp_wait_seconds, cooldown.value
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 48.dp),
                enabledColor = Green,
                onClick = {
                    if (canResend.value) {
                        viewModel.resendOTP(type, email)
                    }
                },
                enabled = canResend.value
            )

            BaseButton(
                onClick = {
                    if (isValidatedInputs) {
                        viewModel.verifyOTP(type, email, token.value)
                        token.value = ""
                    }
                },
                enabled = isValidatedInputs
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
            }
        }

        VerifyOTPKeyboard(token = token)
    }
}

@Composable
private fun VerifyOTPHeader(
    modifier: Modifier = Modifier,
    email: String
) {
    val maskedEmail = EmailUtils.maskEmailWithStars(email)
    val desc = stringResource(R.string.verify_otp_desc, maskedEmail)

    val annotatedDesc = buildAnnotatedString {
        append(desc)
        val startIndex = desc.indexOf(maskedEmail)
        if (startIndex >= 0) {
            val endIndex = startIndex + maskedEmail.length
            addStyle(
                style = SpanStyle(color = Greyscale900),
                start = startIndex,
                end = endIndex
            )
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.verify_otp_title),
            style = Typography.titleMedium
        )
        Text(
            text = annotatedDesc,
            style = Typography.bodyMedium
        )
    }
}

@Composable
private fun VerifyOTPFields(
    modifier: Modifier = Modifier,
    token: MutableState<String>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(6) { index ->
            val tokenLength = token.value.length
            val isFilled = tokenLength - 1 >= index
            val borderColor = when {
                index == tokenLength - 1 -> Green
                else -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Greyscale50)
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isFilled) token.value[index].toString() else ""
                )
            }
        }
    }
}

@Composable
private fun VerifyOTPKeyboard(
    modifier: Modifier = Modifier,
    token: MutableState<String>
) {
    val keys = listOf(
        listOf("1", "4", "7", " "),
        listOf("2", "5", "8", "0"),
        listOf("3", "6", "9", "<")
    )

    fun handleKeyPress(key: String) {
        when (key) {
            "<" -> {
                if (token.value.isNotEmpty()) {
                    token.value = token.value.dropLast(1)
                }
            }
            else -> {
                if (token.value.length < 6) {
                    token.value += key
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            keys.forEach { columnKeys ->
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    columnKeys.forEach { key ->
                        if (key.isNotEmpty()) {
                            KeyboardButton(
                                key = key,
                                modifier = Modifier
                                    .height(56.dp)
                                    .fillMaxWidth(),
                                onClick = { handleKeyPress(key) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyboardButton(
    key: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (key == "<") {
        BaseIconButton(
            modifier = modifier,
            border = null,
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(R.drawable.backspace),
                contentDescription = stringResource(R.string.backspace)
            )
        }
    } else if (key == " ") {
        Text(
            text = key,
            modifier = modifier,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
    else {
        BaseTextButton(
            text = key,
            modifier = modifier,
            contentModifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            enabledColor = Greyscale900,
            onClick = onClick,
        )
    }
}

fun NavController.navigateToVerifyOTP(
    type: OTPType,
    email: String,
    nextDestination: String? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    val route = NavigationRoute.VerifyOTPScreen(type, email, nextDestination).route
    navigate(route = route, navOptions)
}

fun NavGraphBuilder.verifyOTPScreen(
    onNavigateToBack: () -> Unit = {},
    onNavigateToNext: (String) -> Unit
) {
    composable(
        route = "verify_otp/{type}/{email}?next={next}",
        arguments = listOf(
            navArgument("type") { type = NavType.StringType },
            navArgument("email") { type = NavType.StringType },
            navArgument("next") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )
    ) { backStackEntry ->
        val type = backStackEntry.arguments?.getString("type")
        val email = backStackEntry.arguments?.getString("email")
        val nextDestination = backStackEntry.arguments?.getString("next")
        if (type != null && email != null) {
            VerifyOTPScreen(
                type = OTPType.otpTypeFromString(type),
                email = email,
                nextDestination = nextDestination,
                onNavigateToBack = onNavigateToBack,
                onNavigateToNext = onNavigateToNext
            )
        } else {
            //TODO
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyOTPScreenPreview() {
    VerifyOTPScreen(
        type = OTPType.EMAIL,
        email = "example@email.com"
    )
}