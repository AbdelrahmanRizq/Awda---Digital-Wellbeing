package com.awda.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.awda.app.domain.common.models.ChartTimeBase

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun UsageChart(
    data: List<BarData>,
    base: ChartTimeBase = ChartTimeBase.DAY,
    isSingleApp: Boolean = false
) {
    var yStepSize = 0
    var axisStepSize = 0
    var paddingBetweenBars = 0
    var barWidth = 0
    var indicatorLineWidth = 0
    var yLabel: (Int) -> String = { "" }
    when (base) {
        ChartTimeBase.DAY -> {
            yStepSize = 3
            axisStepSize = 2
            paddingBetweenBars = 2
            barWidth = 4
            indicatorLineWidth = 0
            yLabel = { "${it * 10}m" }
        }

        ChartTimeBase.WEEK -> {
            yStepSize = 12
            axisStepSize = 16
            paddingBetweenBars = 16
            barWidth = 20
            indicatorLineWidth = 5
            yLabel = { "${it * 2}h" }
        }

        ChartTimeBase.MONTH -> {
            yStepSize = 12
            axisStepSize = 30
            paddingBetweenBars = 20
            barWidth = 24
            indicatorLineWidth = 5
            yLabel = { "${it * 14}h" }
        }
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(axisStepSize.dp)
        .steps(data.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(48.dp)
        .endPadding(48.dp)
        .labelData { index -> data[index].label }
        .axisLabelColor(MaterialTheme.colorScheme.onPrimary)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .axisLineColor(MaterialTheme.colorScheme.primary)
        .shouldDrawAxisLineTillEnd(true)
        .indicatorLineWidth(indicatorLineWidth.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> yLabel(index) }
        .axisLabelColor(MaterialTheme.colorScheme.onPrimary)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .axisLineColor(MaterialTheme.colorScheme.primary)
        .build()

    val barChartData = BarChartData(
        backgroundColor = MaterialTheme.colorScheme.background,
        chartData = data,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = paddingBetweenBars.dp,
            barWidth = barWidth.dp
        ),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 24.dp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 24.dp),
    ) {

        if (data.isNotEmpty()) {
            Column {
                BarChart(
                    modifier = Modifier
                        .height(350.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    barChartData = barChartData
                )

                val text = when (base) {
                    ChartTimeBase.DAY -> {
                        annotatedString(
                            parameters = arrayOf(
                                Pair(
                                    "Today's ${if (isSingleApp) "app" else "phone"} usage calculated over 30 minutes interval.",
                                    14
                                )
                            )
                        )
                    }

                    ChartTimeBase.WEEK -> {
                        annotatedString(
                            parameters = arrayOf(
                                Pair(
                                    "This Week's ${if (isSingleApp) "app" else "phone"} usage calculated over 1 day interval.",
                                    14
                                )
                            )
                        )
                    }

                    ChartTimeBase.MONTH -> {
                        annotatedString(
                            parameters = arrayOf(
                                Pair(
                                    "This Month's ${if (isSingleApp) "app" else "phone"} usage calculated over 1 week interval.\n\n",
                                    14
                                ),
                                Pair(
                                    "Note: Android limits app usage stats to a week. We're caching data for accurate results, optimal insights in 3 weeks.",
                                    10
                                )
                            )
                        )
                    }
                }

                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}