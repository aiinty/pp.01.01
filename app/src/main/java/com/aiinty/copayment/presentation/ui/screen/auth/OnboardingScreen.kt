package com.aiinty.copayment.presentation.ui.screen.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.aiinty.copayment.R
import com.aiinty.copayment.presentation.navigation.NavigationRoute
import com.aiinty.copayment.presentation.ui.components.base.BaseButton
import com.aiinty.copayment.presentation.ui.components.base.BaseTextButton
import com.aiinty.copayment.presentation.ui.components.base.TopShadow
import com.aiinty.copayment.presentation.ui.theme.Typography
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val onboardingContent = listOf(
        Triple(
            stringResource(R.string.onboarding_item_title_1),
            stringResource(R.string.onboarding_item_desc_1),
            R.drawable.onboarding1
        ),
        Triple(
            stringResource(R.string.onboarding_item_title_2),
            stringResource(R.string.onboarding_item_desc_2),
            R.drawable.onboarding2
        )
    )
    val pageCount = onboardingContent.count()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pageCount }
    )
    val currentHeader = remember {
        derivedStateOf {
            onboardingContent.getOrNull(pagerState.currentPage) ?: onboardingContent[0]
        }
    }

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.End
    ) {
        BaseTextButton(
            text = stringResource(R.string.onboarding_skip),
            modifier = Modifier
                .padding(16.dp),
            onClick = onNavigateToLogin
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            //image on the back of item
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                pageSpacing = 24.dp
            ) { page ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = onboardingContent[page].third),
                    contentScale = ContentScale.Fit,
                    contentDescription = onboardingContent[page].second
                )
            }
            //item content
            TopShadow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.42f)
                    .align(Alignment.BottomCenter)
                    .background(Color.White),
                shadowHeight = 120f,
                shadowColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    AnimatedOnboardingDescItem(
                        modifier = Modifier.fillMaxWidth(),
                        currentHeader = currentHeader.value
                    )

                    DotsIndicator(
                        dotCount = pageCount,
                        type = ShiftIndicatorType(
                            dotsGraphic = DotGraphic(
                                size = 10.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ),
                        pagerState = pagerState
                    )

                    BaseButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage == pageCount - 1) {
                                    onNavigateToLogin()
                                } else {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.onboarding_get_started),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedOnboardingDescItem(
    modifier: Modifier = Modifier,
    itemsArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    currentHeader: Triple<String, String, Int?>
) {
    Column(
        modifier = modifier,
        verticalArrangement = itemsArrangement
    ) {
        AnimatedContent(
            targetState = currentHeader.first,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { targetText ->
            Text(
                text = targetText,
                textAlign = TextAlign.Center,
                style = Typography.titleMedium
            )
        }
        AnimatedContent(
            targetState = currentHeader.second,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { targetText ->
            Text(
                text = targetText,
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium
            )
        }
    }
}

fun NavController.navigateToOnboarding(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = NavigationRoute.OnboardingScreen.route, navOptions)

fun NavGraphBuilder.onboardingScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit = {}
) {
    composable(
        route = NavigationRoute.OnboardingScreen.route
    ) {
        OnboardingScreen(
            modifier = modifier,
            onNavigateToLogin = onNavigateToSignIn
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(
        Modifier.fillMaxSize()
    )
}