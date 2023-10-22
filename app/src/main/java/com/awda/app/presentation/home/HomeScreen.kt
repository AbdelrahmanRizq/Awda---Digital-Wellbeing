package com.awda.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.awda.app.common.isPermissionGranted
import com.awda.app.data.home.models.AddictionLevel
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.presentation.common.components.ErrorPopup
import com.awda.app.presentation.common.components.UsageChart
import com.awda.app.presentation.home.components.AddictionMeasurement
import com.awda.app.presentation.home.components.AppUsageSummary
import com.awda.app.presentation.home.components.AverageUsage
import com.awda.app.presentation.home.components.TodayUsageProgressIndicator
import com.awda.app.presentation.navigation.Screen

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateTo: (route: String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getTodayAppUsage {
            viewModel.getTodayUsageProgress(it)
        }
        viewModel.getYesterdayAppUsage()
        if (viewModel.addictionLevelState.value == AddictionLevel.UNAVAILABLE) {
            viewModel.getAddictionLevel()
        }
        if (viewModel.dayChartDataState.value.isEmpty()) {
            viewModel.getDayUsageChartData()
        }
        if (viewModel.weekChartDataState.value.isEmpty()) {
            viewModel.getWeekUsageChartData()
        }
        if (viewModel.monthChartDataState.value.isEmpty()) {
            viewModel.getMonthUsageChartData()
        }
        if (viewModel.averageWeekUsageState.value == 0L) {
            viewModel.getWeekAverageUsage()
        }
        if (viewModel.averageMonthUsageState.value == 0L) {
            viewModel.getMonthAverageUsage()
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(vertical = 24.dp)
                .verticalScroll(rememberScrollState())
                .height(this@BoxWithConstraints.maxHeight),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var error = viewModel.baseErrorState.value ?: viewModel.errorState.value

            item {
                if (error != null) {
                    ErrorPopup(
                        header = error!!.header,
                        message = error!!.message,
                        permission = error!!.permission
                    ) {
                        if (error!!.permission != null && context.isPermissionGranted(
                                error!!.permission!!
                            )
                        ) {
                            error = null
                            viewModel.clearError()

                            viewModel.getTodayAppUsage {
                                viewModel.getTodayUsageProgress(it)
                            }
                            viewModel.getYesterdayAppUsage()
                            viewModel.getAddictionLevel()
                            viewModel.getDayUsageChartData()
                            viewModel.getWeekUsageChartData()
                            viewModel.getMonthUsageChartData()
                            viewModel.getWeekAverageUsage()
                            viewModel.getMonthAverageUsage()
                        }
                    }
                }
            }

            item {
                val (progress, text) = viewModel.usageTodayProgressState.value
                TodayUsageProgressIndicator(
                    modifier = Modifier
                        .padding(all = 24.dp)
                        .clickable(
                            indication = rememberRipple(bounded = false),
                            interactionSource = remember {
                                MutableInteractionSource()
                            }, onClick = {
                                navigateTo(Screen.Timeline.route)
                            }
                        ),
                    progress = progress,
                    text = text
                )
            }

            item {
                AppUsageSummary(viewModel, navigateTo)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                AddictionMeasurement(level = viewModel.addictionLevelState.value) {
                    navigateTo(Screen.Addiction.route)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                UsageChart(data = viewModel.dayChartDataState.value)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                UsageChart(
                    data = viewModel.weekChartDataState.value,
                    base = ChartTimeBase.WEEK
                )
                Spacer(modifier = Modifier.height(24.dp))

            }

            item {
                AverageUsage(time = viewModel.averageWeekUsageState.value, week = true)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                UsageChart(
                    data = viewModel.monthChartDataState.value,
                    base = ChartTimeBase.MONTH
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                AverageUsage(
                    time = viewModel.averageMonthUsageState.value,
                    week = false
                )
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}