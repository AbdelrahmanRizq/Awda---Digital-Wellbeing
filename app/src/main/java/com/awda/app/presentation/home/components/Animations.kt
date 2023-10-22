package com.awda.app.presentation.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.presentation.theme.DarkBlack
import com.awda.app.presentation.theme.DarkBlue

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppUsageProgressIndicator(
    modifier: Modifier,
    apps: List<CombinedAppUsage>
) {
    val appPercentages = getAppUsagePercentages(
        if (apps.size >= 3)
            apps.subList(0, 3)
        else
            apps
    )

    var firstColorStop by remember { mutableStateOf(0f) }
    var secondColorStop by remember { mutableStateOf(0f) }
    var thirdColorStop by remember { mutableStateOf(0f) }

    var progress by remember {
        mutableStateOf(0f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val width = LocalConfiguration.current.screenWidthDp.dp

    LaunchedEffect(Unit) {
        firstColorStop = appPercentages.getOrElse(0) { 0f }
        secondColorStop = appPercentages.getOrElse(1) { 0f } + firstColorStop
        thirdColorStop = appPercentages.getOrElse(2) { 0f } + secondColorStop
        progress = 1f
    }

    Box(
        modifier = modifier
            .background(Color.Gray)
            .width(width)
    ) {
        Box(
            modifier = modifier
                .background(
                    Brush.horizontalGradient(
                        colorStops = arrayOf(
                            Pair(
                                firstColorStop,
                                MaterialTheme.colorScheme.primary
                            ),
                            Pair(
                                secondColorStop,
                                MaterialTheme.colorScheme.secondary
                            ),
                            Pair(
                                thirdColorStop,
                                MaterialTheme.colorScheme.tertiary
                            ),
                        ),
                    )
                )
                .width(width * animatedProgress)
        )
    }
}

@Composable
fun TodayUsageProgressIndicator(
    modifier: Modifier,
    progress: Float,
    text: String
) {
    val size: Dp = 260.dp
    val indicatorThickness: Dp = 24.dp

    var progressState by mutableStateOf(progress)

    LaunchedEffect(Unit) {
        progressState = progress
    }

    val progressAnimate = animateFloatAsState(
        targetValue = progressState,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    Box(
        modifier = modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(size)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.LightGray, Color.White),
                    center = Offset(x = this.size.width / 2, y = this.size.height / 2),
                    radius = this.size.height / 2
                ),
                radius = this.size.height / 2,
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            drawCircle(
                color = DarkBlack,
                radius = (size / 2 - indicatorThickness).toPx(),
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            val sweepAngle = (progressAnimate.value) * 360 / 100

            drawArc(
                color = DarkBlue,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
                size = Size(
                    width = (size - indicatorThickness).toPx(),
                    height = (size - indicatorThickness).toPx()
                ),
                topLeft = Offset(
                    x = (indicatorThickness / 2).toPx(),
                    y = (indicatorThickness / 2).toPx()
                )
            )
        }

        Text(
            text = "$text\n\nTap to see\nusage report",
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private fun getAppUsagePercentages(apps: List<CombinedAppUsage>) =
    convertUsageTimeToPercentage(apps.map { it.totalUsage })


private fun convertUsageTimeToPercentage(timestamps: List<Long>) =
    timestamps.map { it.toFloat() / timestamps.sum().toFloat() }