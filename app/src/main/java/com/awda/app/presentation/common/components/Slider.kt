package com.awda.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun TimeLimitSlider(
    time: Long,
    onDismissRequest: (Long) -> Unit
) {
    var value by remember { mutableStateOf(convertMillisToSliderValue(time)) }

    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties(focusable = true),
        onDismissRequest = {
            onDismissRequest(convertSliderValueToMillis(value))
        }
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Daily Usage Time Limit",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        value = value,
                        onValueChange = { newValue ->
                            value = newValue
                        },
                        valueRange = 1f..32f,
                        steps = 30
                    )

                    Text(
                        text = "${convertSliderValueToTimeString(value)}\nHOURS",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp)
                ) {
                    BorderlessTransparentButton(
                        modifier = Modifier,
                        text = "DONE",
                        enabled = true
                    ) {
                        onDismissRequest(convertSliderValueToMillis(value))
                    }
                }
            }
        }
    }
}

private fun convertMillisToSliderValue(millis: Long): Float {
    return (millis / 900000).toFloat()
}

private fun convertSliderValueToMillis(value: Float): Long {
    val (hours, minutes) = convertSliderValueToClockPair(value)
    return (hours * 60 * 60 + minutes * 60) * 1000L
}

private fun convertSliderValueToTimeString(value: Float): String {
    val (hours, minutes) = convertSliderValueToClockPair(value)
    return String.format("%02d:%02d", hours, minutes)
}

private fun convertSliderValueToClockPair(time: Float): Pair<Int, Int> {
    val totalMinutes = (time * 15).toInt()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return Pair(hours, minutes)
}