package com.awda.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker

/**
 * Created by Abdelrahman Rizq
 */

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NumberPickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onPick: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedHour by remember {
        mutableStateOf(initialHour)
    }

    var selectedMinute by remember {
        mutableIntStateOf(initialMinute)
    }

    var pickerValue by remember { mutableStateOf<Hours>(FullHours(selectedHour, selectedMinute)) }


    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(24.dp)),
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
                .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HoursNumberPicker(
                dividersColor = MaterialTheme.colorScheme.primary,
                leadingZero = false,
                value = pickerValue,
                onValueChange = {
                    pickerValue = it
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                hoursDivider = {
                    Text(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        text = ":"
                    )
                }
            )

            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Dismiss")
                }


                TextButton(
                    onClick = {
                        selectedHour = pickerValue.hours
                        selectedMinute = pickerValue.minutes
                        onPick(selectedHour, selectedMinute)
                    }
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}