package com.awda.app.presentation.settings.components

import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import com.awda.app.common.openPermissionSettings
import com.awda.app.common.parseClockToMillis
import com.awda.app.common.parseMillisToClock
import com.awda.app.data.challenge.models.BlockConfiguration
import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.presentation.common.components.NumberPickerDialog
import com.awda.app.presentation.common.components.TimePickerDialog
import com.awda.app.presentation.intro.components.observeAsState
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun BlockedAppItem(
    app: InstalledApp,
    selected: Boolean,
    onSelect: (Boolean) -> Unit,
    onChange: (InstalledApp) -> Unit = {}
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var showNumberPicker by remember { mutableStateOf(false) }
    var blockNotifications by remember { mutableStateOf(app.block.blockNotifications) }
    var selectedStartHour by remember { mutableStateOf(parseMillisToClock(app.block.startTimeOfDay).first) }
    var selectedStartMinute by remember { mutableStateOf(parseMillisToClock(app.block.startTimeOfDay).second) }
    var selectedHours by remember { mutableStateOf(parseMillisToClock(app.block.duration).first) }
    var selectedMinutes by remember { mutableStateOf(parseMillisToClock(app.block.duration).second) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(
                if (selected) {
                    BorderStroke(
                        1.dp,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    BorderStroke(0.dp, Color.Transparent)
                },
                RoundedCornerShape(24.dp)
            )
    ) {
        ListItem(
            modifier = Modifier
                .clickable(
                    onClick = {
                        if (!selected) {
                            val block = BlockConfiguration()
                            val (startHour, startMinute) = parseMillisToClock(block.startTimeOfDay)
                            val (hours, minutes) = parseMillisToClock(block.duration)
                            selectedStartHour = startHour
                            selectedStartMinute = startMinute
                            selectedHours = hours
                            selectedMinutes = minutes
                        }
                        onSelect(!selected)
                    }
                )
                .fillMaxWidth(),
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            leadingContent = {
                Image(
                    modifier = Modifier.size(72.dp),
                    painter = rememberDrawablePainter(drawable = app.icon),
                    contentDescription = null
                )
            },
            headlineContent = { Text(app.name) }
        )

        AnimatedVisibility(
            visible = selected,
            enter = expandVertically(animationSpec = tween(700)),
            exit = shrinkVertically(animationSpec = tween(700))
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {

                Weekdays(
                    app = app,
                    onChanged = {
                        onChange(it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DurationConfiguration(
                    description = "From",
                    value = if (selectedStartHour == -1 || selectedStartMinute == -1)
                        "Now"
                    else
                        "$selectedStartHour:$selectedStartMinute",
                    onClick = {
                        showTimePicker = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DurationConfiguration(
                    description = "For",
                    value = "${selectedHours}h ${selectedMinutes}m",
                    onClick = {
                        showNumberPicker = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                NotificationConfiguration(
                    blocked = blockNotifications,
                    onValueChanged = {
                        blockNotifications = it
                        app.block.blockNotifications = it
                        onChange(app)
                    }
                )
            }
        }

        if (showNumberPicker) {
            NumberPickerDialog(
                initialHour = selectedHours,
                initialMinute = selectedMinutes,
                onPick = { hour, minute ->
                    selectedHours = hour
                    selectedMinutes = minute
                    app.block.duration = parseClockToMillis(selectedHours, selectedMinutes)
                    onChange(app)
                    showNumberPicker = false
                },
                onDismiss = {
                    showNumberPicker = false
                }
            )
        }

        if (showTimePicker) {
            TimePickerDialog(
                initialHour = if (selectedStartHour == -1)
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                else
                    selectedStartHour,
                initialMinute = if (selectedStartMinute == -1)
                    Calendar.getInstance().get(Calendar.MINUTE)
                else
                    selectedStartMinute,
                onPick = { hour, minute ->
                    selectedStartHour = hour
                    selectedStartMinute = minute
                    app.block.startTimeOfDay =
                        parseClockToMillis(selectedStartHour, selectedStartMinute)
                    onChange(app)
                    showTimePicker = false
                },
                onDismiss = {
                    showTimePicker = false
                }
            )
        }
    }

}

@Composable
private fun DurationConfiguration(description: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick()
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = description)

        Row {
            Text(text = value)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun NotificationConfiguration(blocked: Boolean, onValueChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    var enabled by mutableStateOf(blocked)
    var permissionGranted by mutableStateOf(
        NotificationManagerCompat.getEnabledListenerPackages(context)
            .contains(context.packageName)
    )
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
    LaunchedEffect(key1 = lifecycleState.value, block = {
        permissionGranted = NotificationManagerCompat.getEnabledListenerPackages(context)
            .contains(context.packageName)
    })
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Block Notifications")

            Switch(
                checked = enabled,
                onCheckedChange = {
                    enabled = it
                    onValueChanged(it)
                },
            )
        }

        if (!permissionGranted && enabled) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        context.openPermissionSettings(
                            Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS,
                            highlightPackage = false
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notification Listener Permission Not Granted",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}