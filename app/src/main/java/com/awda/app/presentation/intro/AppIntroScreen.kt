package com.awda.app.presentation.intro

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.awda.app.common.Constants
import com.awda.app.common.isPermissionGranted
import com.awda.app.presentation.common.components.BorderlessTransparentButton
import com.awda.app.presentation.intro.components.AppIntroAnimation
import com.awda.app.presentation.intro.components.AppIntroPage
import com.awda.app.presentation.intro.components.AppIntroPager
import com.awda.app.presentation.intro.components.PagesIndicator

import kotlinx.coroutines.launch

/**
 * Created by Abdelrahman Rizq
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppIntroScreen(
    viewModel: AppIntroViewModel,
    onTimeSet: (Long) -> Unit,
    navigateToHome: () -> Unit
) {
    val pageCount = 4
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current as Activity
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Column(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    AppIntroAnimation("intro_1.json")
                    AppIntroPager(
                        state = pagerState,
                        pages = listOf(
                            {
                                AppIntroPage(
                                    viewModel = viewModel,
                                    text = "Welcome to Awda, we are here to help you find a healthier balance with your device usage. By tracking your mobile habits, we can provide insights and tools to promote a more mindful and balanced lifestyle.",
                                    permission = null
                                )
                            },
                            {
                                AppIntroPage(
                                    viewModel = viewModel,
                                    text = "To get started, we require your permission to access your package usage data.",
                                    permission = Constants.PACKAGE_USAGE_PERMISSION
                                )
                            },
                            {
                                AppIntroPage(
                                    viewModel = viewModel,
                                    text = "Enable overlay access to show alert notification.",
                                    permission = Constants.OVERLAY_PERMISSION
                                )
                            }, {
                                AppIntroPage(
                                    viewModel = viewModel,
                                    text = "Tell us how much time you want to spend on your phone and Awda will help you meet that goal.",
                                    permission = null,
                                    timePage = true,
                                    onTimeSet = onTimeSet
                                )
                            }
                        )
                    )
                }
            }
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                PagesIndicator(
                    modifier = Modifier.weight(1f),
                    state = pagerState,
                    pageCount = pageCount
                )
                BorderlessTransparentButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp),
                    text = "NEXT",
                ) {
                    scope.launch {
                        if (pagerState.currentPage >= pageCount - 1) {
                            navigateToHome()
                        } else {
                            if (pagerState.currentPage == 1 && !context.isPermissionGranted(
                                    Constants.PACKAGE_USAGE_PERMISSION
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    "Please grant package usage permission to proceed",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (pagerState.currentPage == 2 && !context.isPermissionGranted(
                                    Constants.OVERLAY_PERMISSION
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    "Please grant overlay permission to proceed",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                }
            }
        }
    )
}
