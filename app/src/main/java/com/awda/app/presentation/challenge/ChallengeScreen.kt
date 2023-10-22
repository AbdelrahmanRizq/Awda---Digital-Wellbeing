package com.awda.app.presentation.challenge

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.awda.app.common.Constants
import com.awda.app.common.openPermissionSettings
import com.awda.app.presentation.challenge.components.ChallengeList
import com.awda.app.presentation.challenge.components.ChallengeReport
import com.awda.app.presentation.common.components.ErrorPopup
import com.awda.app.presentation.navigation.Screen

/**
 * Created by Abdelrahman Rizq
 */

@SuppressLint("InlinedApi")
@Composable
fun ChallengeScreen(
    viewModel: ChallengeViewModel,
    navigateTo: (route: String) -> Unit
) {
    val context = LocalContext.current
    var openErrorPopup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getChallenges()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 96.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val alarmManager =
                            ContextCompat.getSystemService(context, AlarmManager::class.java)
                        if (alarmManager?.canScheduleExactAlarms() == false) {
                            openErrorPopup = true
                        } else {
                            navigateTo(Screen.AddChallenge.route)
                        }
                    } else {
                        navigateTo(Screen.AddChallenge.route)
                    }
                }, content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            )
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (viewModel.challengeState.value.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "Add a challenge to start",
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                    } else {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.surface)
                                    .padding(vertical = 24.dp)
                                    .verticalScroll(rememberScrollState())
                                    .height(this@BoxWithConstraints.maxHeight),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {

                                item {
                                    ChallengeReport(viewModel = viewModel)
                                    Spacer(modifier = Modifier.height(24.dp))
                                }

                                viewModel.challengeState.value.groupBy { it.month() }.forEach {
                                    item {
                                        ChallengeList(
                                            challenges = it.value,
                                            month = it.key,
                                            onEnableChallenge = { challenge, enabled ->
                                                viewModel.enableChallenge(challenge, enabled)
                                            }
                                        )
                                    }
                                }

                                item {
                                    Spacer(modifier = Modifier.height(96.dp))
                                }
                            }
                        }
                    }


                    if (openErrorPopup) {
                        ErrorPopup(
                            header = "Schedule Permission Not Granted",
                            message = "Please grant schedule permission to schedule added app challenges.",
                            permission = Constants.OVERLAY_PERMISSION
                        ) {
                            openErrorPopup = false
                            context.openPermissionSettings(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        }
                    }
                }
            }
        }
    )
}
