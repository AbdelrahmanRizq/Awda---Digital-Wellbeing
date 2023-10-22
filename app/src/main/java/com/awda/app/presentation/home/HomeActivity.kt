package com.awda.app.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.awda.app.common.Constants
import com.awda.app.common.isPermissionGranted
import com.awda.app.domain.service.AppLockerService
import com.awda.app.domain.service.ChallengeService
import com.awda.app.domain.worker.AppUsageWorker
import com.awda.app.presentation.challenge.ChallengeViewModel
import com.awda.app.presentation.navigation.AppNavigation
import com.awda.app.presentation.settings.SettingsViewModel
import com.awda.app.presentation.theme.AwdaTheme
import com.awda.app.presentation.usage.AppUsageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

class HomeActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val appUsageViewModel: AppUsageViewModel by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val challengeViewModel: ChallengeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AwdaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(
                        homeViewModel = homeViewModel,
                        appUsageViewModel = appUsageViewModel,
                        settingsViewModel = settingsViewModel,
                        challengeViewModel = challengeViewModel,
                        enableLockerService = {
                            startLockerService(it)
                        },
                        enableChallengeService = {
                            startChallengeService(it)
                        }
                    )
                }
            }
        }

        startLockerService(settingsViewModel.usageAlertState.value && isPermissionGranted(Constants.OVERLAY_PERMISSION))
        startChallengeService(true)

        WorkManager.getInstance(this).enqueue(
            PeriodicWorkRequest.Builder(
                AppUsageWorker::class.java,
                1,
                TimeUnit.DAYS
            ).build()
        )
    }

    private fun startLockerService(enabled: Boolean) {
        val intent = Intent(this, AppLockerService::class.java)
        if (enabled) {
            startService(intent)
        } else {
            stopService(intent)
        }
    }

    private fun startChallengeService(enabled: Boolean) {
        val intent = Intent(this, ChallengeService::class.java)
        if (enabled) {
            startService(intent)
        } else {
            stopService(intent)
        }
    }
}