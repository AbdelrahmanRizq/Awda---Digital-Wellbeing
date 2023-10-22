package com.awda.app.presentation.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.awda.app.presentation.home.HomeActivity
import com.awda.app.presentation.theme.AwdaTheme

import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Abdelrahman Rizq
 */

class AppIntroActivity : ComponentActivity() {
    private val viewModel: AppIntroViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.appIntroCompletionState.value) {
            navigateToHome()
        }
        setContent {
            AwdaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppIntroScreen(
                        viewModel = viewModel,
                        onTimeSet = {
                            viewModel.setUsageTimeLimit(it)
                        },
                        navigateToHome = {
                            viewModel.setAppIntroCompleted()
                            navigateToHome()
                        }
                    )
                }
            }
        }
    }

    private fun navigateToHome() {
        startActivity(
            Intent(
                this,
                HomeActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }
}