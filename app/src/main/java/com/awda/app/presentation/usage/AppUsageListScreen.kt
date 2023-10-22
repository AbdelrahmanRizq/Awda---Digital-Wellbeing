package com.awda.app.presentation.usage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.presentation.common.components.AppUsageList
import com.awda.app.presentation.usage.components.AppUsageSearchBar
import com.skydoves.orbital.Orbital

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppUsageListScreen(
    viewModel: AppUsageViewModel,
    sharedImage: @Composable (Int) -> Unit,
    navigateToDetailsScreen: (CombinedAppUsage) -> Unit
) {
    Orbital {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Surface(
                    modifier = Modifier
                        .padding(it)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(vertical = 24.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppUsageSearchBar(apps = viewModel.appUsageState.value) {
                            navigateToDetailsScreen(it)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(24.dp))
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(all = 24.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            AppUsageList(
                                apps = viewModel.appUsageState.value,
                                maxAppDisplayCount = viewModel.appUsageState.value.size,
                                scrollable = true,
                                sharedImage = sharedImage
                            ) {
                                navigateToDetailsScreen(it)
                            }
                        }
                    }
                }
            }
        )
    }
}