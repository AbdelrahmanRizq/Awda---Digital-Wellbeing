package com.awda.app.presentation.usage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.awda.app.common.isPermissionGranted
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.presentation.common.components.ErrorPopup
import com.awda.app.presentation.common.components.UsageChart
import com.awda.app.presentation.common.components.UsageText
import com.awda.app.presentation.usage.components.AppUsageDetails
import com.skydoves.orbital.Orbital

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppUsageScreen(
    viewModel: AppUsageViewModel,
    app: CombinedAppUsage,
    sharedImage: @Composable () -> Unit,
    navigateToListScreen: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.selectApp(app)
        viewModel.getWeekTotalAppUsage()
        viewModel.getMonthTotalAppUsage()
        viewModel.getWeekAverageAppUsagePerDay()
        viewModel.getMonthAverageAppUsagePerDay()
        viewModel.getDayAppUsageChartData()
        viewModel.getWeekAppUsageChartData()
        viewModel.getMonthAppUsageChartData()
    }

    BackHandler {
        navigateToListScreen()
    }

    Orbital {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Surface(
                    modifier = Modifier
                        .padding(it)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(vertical = 24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        var error = viewModel.errorState.value

                        if (error != null) {
                            ErrorPopup(
                                header = error.header,
                                message = error.message,
                                permission = error.permission
                            ) {
                                if (error!!.permission != null && context.isPermissionGranted(error!!.permission!!)) {
                                    viewModel.getWeekTotalAppUsage()
                                    viewModel.getMonthTotalAppUsage()
                                    viewModel.getWeekAverageAppUsagePerDay()
                                    viewModel.getMonthAverageAppUsagePerDay()
                                    viewModel.getDayAppUsageChartData()
                                    viewModel.getWeekAppUsageChartData()
                                    viewModel.getMonthAppUsageChartData()
                                }

                                error = null
                                viewModel.clearError()
                            }
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        sharedImage()

                        Spacer(modifier = Modifier.height(8.dp))

                        UsageText(time = app.totalUsage)

                        Spacer(modifier = Modifier.height(48.dp))

                        AppUsageDetails(
                            week = true,
                            usagePerDay = viewModel.weekAverageAppUsagePerDayState.value,
                            totalUsage = viewModel.weekTotalAppUsageState.value
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        AppUsageDetails(
                            week = false,
                            usagePerDay = viewModel.monthAverageAppUsagePerDayState.value,
                            totalUsage = viewModel.monthTotalAppUsageState.value
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        UsageChart(data = viewModel.dayAppChartDataState.value, isSingleApp = true)

                        Spacer(modifier = Modifier.height(24.dp))

                        UsageChart(
                            data = viewModel.weekAppChartDataState.value,
                            base = ChartTimeBase.WEEK,
                            isSingleApp = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        UsageChart(
                            data = viewModel.monthAppChartDataState.value,
                            base = ChartTimeBase.MONTH,
                            isSingleApp = true
                        )
                    }
                }
            }
        )
    }
}