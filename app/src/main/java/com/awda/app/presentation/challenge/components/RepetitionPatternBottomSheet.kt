package com.awda.app.presentation.challenge.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.awda.app.data.challenge.models.CustomRepetitionPattern
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import com.awda.app.data.challenge.models.RepetitionPattern
import kotlinx.coroutines.launch

/**
 * Created by Abdelrahman Rizq
 */

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RepetitionPatternBottomSheet(
    onSelect: (RepetitionPattern) -> Unit,
    onDismiss: () -> Unit
) {
    val repeatOptions = listOf(
        DefaultRepetitionPattern.ONCE.capitalizedName(),
        DefaultRepetitionPattern.DAILY.capitalizedName(),
        DefaultRepetitionPattern.WORKDAYS.capitalizedName(),
        DefaultRepetitionPattern.WEEKENDS.capitalizedName(),
        DefaultRepetitionPattern.CUSTOM.capitalizedName()
    )

    var selectedRepeatOption by remember { mutableStateOf(DefaultRepetitionPattern.ONCE.capitalizedName()) }
    var custom by mutableStateOf(false)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {

        if (!custom) {
            val onSelectOption: (String) -> Unit = {
                selectedRepeatOption = it
                if (selectedRepeatOption == DefaultRepetitionPattern.CUSTOM.capitalizedName()) {
                    custom = true
                } else {
                    scope
                        .launch {
                            sheetState.hide()
                        }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onSelect(RepetitionPattern(default = DefaultRepetitionPattern.entries.first { it.name.lowercase() == selectedRepeatOption.lowercase() }))
                            }
                        }
                }
            }

            repeatOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onSelectOption(option)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = option
                    )

                    RadioButton(
                        selected = selectedRepeatOption == option,
                        onClick = {
                            onSelectOption(option)
                        }
                    )
                }
            }

        } else {
            val selectedDays by remember { mutableStateOf(mutableListOf<String>()) }
            val weekDays = listOf(
                CustomRepetitionPattern.MON.capitalizedName(),
                CustomRepetitionPattern.TUE.capitalizedName(),
                CustomRepetitionPattern.WED.capitalizedName(),
                CustomRepetitionPattern.THU.capitalizedName(),
                CustomRepetitionPattern.FRI.capitalizedName(),
                CustomRepetitionPattern.SAT.capitalizedName(),
                CustomRepetitionPattern.SUN.capitalizedName()
            )
            val onSelectOption: (String, Boolean) -> Unit = { day, checked ->
                if (checked) {
                    selectedDays.add(day)
                } else {
                    selectedDays.remove(day)
                }
            }
            weekDays.forEach { day ->
                val isSelectedDay = selectedDays.contains(day)
                var isSelectedState by remember { mutableStateOf(isSelectedDay) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            isSelectedState = !isSelectedState
                            onSelectOption(day, isSelectedState)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = day
                    )

                    Checkbox(
                        checked = isSelectedState,
                        onCheckedChange = {
                            isSelectedState = it
                            onSelectOption(day, it)
                        },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }
                    }, content = {
                        Text(text = "Cancel")
                    }
                )

                TextButton(
                    onClick = {
                        onSelect(
                            RepetitionPattern(
                                custom = CustomRepetitionPattern.entries.filter {
                                    selectedDays.map { it.lowercase() }.contains(
                                        it.name.lowercase()
                                    )
                                }
                            )
                        )
                    }, content = {
                        Text(text = "Done")
                    }
                )
            }
        }
    }
}