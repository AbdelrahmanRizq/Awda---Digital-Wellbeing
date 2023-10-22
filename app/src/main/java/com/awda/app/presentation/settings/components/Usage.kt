package com.awda.app.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awda.app.common.Constants
import com.awda.app.common.isPermissionGranted
import com.awda.app.common.parseMillisToFormattedClock
import com.awda.app.presentation.settings.SettingsViewModel

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun Usage(viewModel: SettingsViewModel, onGoalClick: () -> Unit, onAlertSwitch: (Boolean) -> Unit) {
    val context = LocalContext.current
    var usageAlertEnabled by mutableStateOf(
        viewModel.usageAlertState.value &&
                context.isPermissionGranted(Constants.OVERLAY_PERMISSION)
    )

    LaunchedEffect(key1 = usageAlertEnabled, block = {
        usageAlertEnabled =
            viewModel.usageAlertState.value && context.isPermissionGranted(Constants.OVERLAY_PERMISSION)
    })

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .clickable(
                    indication = rememberRipple(bounded = false),
                    interactionSource = remember {
                        MutableInteractionSource()
                    }, onClick = {
                        onGoalClick()
                    }
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Set Usage Goal",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Daily Usage Limit", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = parseMillisToFormattedClock(viewModel.usageTimeLimitState.value),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Show Alert Notification",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
                Switch(
                    checked = usageAlertEnabled,
                    onCheckedChange = {
                        usageAlertEnabled = it
                        onAlertSwitch(it)
                    },
                )
            }
        }
    }

}