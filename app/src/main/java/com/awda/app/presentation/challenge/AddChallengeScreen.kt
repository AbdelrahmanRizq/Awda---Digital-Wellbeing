package com.awda.app.presentation.challenge

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.awda.app.common.parseClockToMillis
import com.awda.app.common.parseClockToTimestamp
import com.awda.app.common.parseTimestampToFormattedClock
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.challenge.models.AppChallengeState
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.challenge.models.RepetitionPattern
import com.awda.app.presentation.challenge.components.AppBottomSheet
import com.awda.app.presentation.challenge.components.AppsGrid
import com.awda.app.presentation.challenge.components.ChallengeSelection
import com.awda.app.presentation.challenge.components.RepetitionPatternBottomSheet
import com.awda.app.presentation.common.components.MainActionButton
import com.awda.app.presentation.common.components.NumberPickerDialog
import com.awda.app.presentation.common.components.TimePickerDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AddChallengeScreen(
    viewModel: ChallengeViewModel,
    navController: NavController,
    apps: MutableList<InstalledApp>,
    enableChallengeService: (Boolean) -> Unit
) {
    var selectedApp by remember { mutableStateOf<InstalledApp?>(null) }

    var selectedStartHour by remember { mutableStateOf(-1) }

    var selectedStartMinute by remember { mutableStateOf(-1) }

    var selectedHours by remember { mutableStateOf(2) }

    var selectedMinutes by remember { mutableStateOf(0) }

    var showTimePicker by remember { mutableStateOf(false) }

    var showAppsBottomSheet by remember { mutableStateOf(false) }

    var showNumberPicker by remember { mutableStateOf(false) }

    var showRepetitionPatternBottomSheet by remember { mutableStateOf(false) }

    var selectedRepetitionPattern by remember { mutableStateOf(RepetitionPattern(default = DefaultRepetitionPattern.ONCE)) }

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (apps.isEmpty()) {
            viewModel.getInstalledApps()
        } else {
            viewModel.setApps(apps)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

                    if (showAppsBottomSheet) {
                        AppBottomSheet(
                            apps = viewModel.appsState.value,
                            onClick = {
                                selectedApp = it
                                viewModel.selectApp(selectedApp!!)
                                showAppsBottomSheet = false
                            }
                        ) {
                            showAppsBottomSheet = false
                        }
                    }

                    if (viewModel.appsState.value.isNotEmpty()) {
                        AppsGrid(
                            apps = viewModel.appsState.value,
                            onSelect = {
                                selectedApp = it
                                viewModel.selectApp(selectedApp!!)
                            },
                            onShowMoreApps = {
                                showAppsBottomSheet = true
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
                                showTimePicker = false
                            },
                            onDismiss = {
                                showTimePicker = false
                            }
                        )
                    }

                    ChallengeSelection(
                        header = "Select When To Start The Challenge",
                        description = "Start Time",
                        value = if (selectedStartHour == -1 || selectedStartMinute == -1)
                            "Now"
                        else
                            parseTimestampToFormattedClock(
                                parseClockToTimestamp(
                                    selectedStartHour,
                                    selectedStartMinute
                                )
                            ),
                        onClick = {
                            showTimePicker = true
                        }
                    )

                    if (showRepetitionPatternBottomSheet) {
                        RepetitionPatternBottomSheet(
                            onSelect = {
                                showRepetitionPatternBottomSheet = false
                                selectedRepetitionPattern = it
                            },
                            onDismiss = {
                                showRepetitionPatternBottomSheet = false
                            }
                        )
                    }

                    ChallengeSelection(
                        header = "Select Challenge Repetition Pattern",
                        description = "Repeat",
                        value = if (selectedRepetitionPattern.default == DefaultRepetitionPattern.CUSTOM) {
                            if (selectedRepetitionPattern.custom.isEmpty()) {
                                DefaultRepetitionPattern.ONCE.capitalizedName()
                            } else {
                                selectedRepetitionPattern.custom.joinToString(", ") {
                                    it.name.lowercase()
                                        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                                }
                            }
                        } else {
                            selectedRepetitionPattern.default.name.lowercase(Locale.getDefault())
                                .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                        },
                        onClick = {
                            showRepetitionPatternBottomSheet = true
                        }
                    )

                    if (showNumberPicker) {
                        NumberPickerDialog(
                            initialHour = selectedHours,
                            initialMinute = selectedMinutes,
                            onPick = { hour, minute ->
                                selectedHours = hour
                                selectedMinutes = minute
                                showNumberPicker = false
                            },
                            onDismiss = {
                                showNumberPicker = false
                            }
                        )
                    }

                    ChallengeSelection(
                        header = "Select Challenge Time",
                        description = "Don't Use App For",
                        value = "${selectedHours}h ${selectedMinutes}m",
                        onClick = {
                            showNumberPicker = true
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MainActionButton(
                        text = "Done",
                        onClick = {
                            if (selectedApp == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("You must select an app to schedule the app challenge.")
                                }
                            } else if (selectedRepetitionPattern.default == DefaultRepetitionPattern.CUSTOM && selectedRepetitionPattern.custom.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("You must select the repetition pattern of the app challenge.")
                                }
                            } else {
                                enableChallengeService(true)
                                viewModel.setChallenge(
                                    challenge = AppChallenge(
                                        app = selectedApp!!,
                                        createdAt = System.currentTimeMillis(),
                                        occurrence = if (selectedStartHour == -1 || selectedStartMinute == -1)
                                            System.currentTimeMillis() + 10 * 1000
                                        else
                                            parseClockToTimestamp(
                                                selectedStartHour,
                                                selectedStartMinute
                                            ),
                                        time = parseClockToMillis(
                                            selectedHours,
                                            selectedMinutes
                                        ),
                                        repetitionPattern = selectedRepetitionPattern,
                                        state = AppChallengeState.PENDING
                                    )
                                )

                                navController.popBackStack()
                            }
                        }
                    )
                }
            }
        }
    )
}