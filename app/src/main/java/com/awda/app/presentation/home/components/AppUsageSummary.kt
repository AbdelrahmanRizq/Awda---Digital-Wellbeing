package com.awda.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.awda.app.presentation.common.components.AppUsageList
import com.awda.app.presentation.common.components.UsageComparisonText
import com.awda.app.presentation.common.components.UsageText
import com.awda.app.presentation.home.HomeViewModel
import com.awda.app.presentation.navigation.Screen

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppUsageSummary(
    viewModel: HomeViewModel,
    navigateTo: (route: String) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 24.dp)
            .clickable(
                indication = rememberRipple(bounded = false),
                interactionSource = remember {
                    MutableInteractionSource()
                }, onClick = {
                    navigateTo(Screen.AppUsage.route)
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        UsageText(viewModel.usageTimeTodayState.value)

        UsageComparisonText(
            viewModel.usageTimeTodayState.value,
            viewModel.usageTimeYesterdayState.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.appUsageState.value.isNotEmpty()) {
            AppUsageProgressIndicator(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .height(24.dp),
                apps = viewModel.appUsageState.value
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppUsageList(
            apps = viewModel.appUsageState.value,
            maxAppDisplayCount = 3,
            scrollable = false
        )
    }
}