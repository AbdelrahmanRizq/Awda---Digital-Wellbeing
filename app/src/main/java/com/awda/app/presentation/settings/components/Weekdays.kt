package com.awda.app.presentation.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awda.app.data.challenge.models.CustomRepetitionPattern
import com.awda.app.data.challenge.models.InstalledApp
import java.util.Calendar


/**
 * Created by Abdelrahman Rizq
 */


@Composable
fun Weekdays(
    app: InstalledApp,
    onChanged: (InstalledApp) -> Unit
) {
    val weekdays = listOf(
        CustomRepetitionPattern.SUN.firstChar(),
        CustomRepetitionPattern.MON.firstChar(),
        CustomRepetitionPattern.TUE.firstChar(),
        CustomRepetitionPattern.WED.firstChar(),
        CustomRepetitionPattern.THU.firstChar(),
        CustomRepetitionPattern.FRI.firstChar(),
        CustomRepetitionPattern.SAT.firstChar()
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekdays.forEachIndexed { index, day ->
            var isChecked by remember { mutableStateOf(app.block.days.find { it.value == index + 1 } != null) }

            OutlinedButton(
                modifier = Modifier.size(36.dp),
                onClick = {
                    isChecked = !isChecked
                    val updatedDays = if (isChecked) {
                        (app.block.days + CustomRepetitionPattern.values()
                            .first { it.value == index + 1 }).toMutableList()
                    } else {
                        app.block.days.filter {
                            it != CustomRepetitionPattern.values().first { it.value == index + 1 }
                        }.toMutableList()
                    }

                    if (updatedDays.isEmpty()) {
                        updatedDays.add(
                            CustomRepetitionPattern.values().first {
                                it.value == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                            }
                        )
                    }

                    onChanged(
                        app.copy(
                            block = app.block.copy(
                                days = updatedDays
                            )
                        )
                    )
                },
                shape = CircleShape,
                border = if (isChecked) BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ) else null,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Text(
                    text = day.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}