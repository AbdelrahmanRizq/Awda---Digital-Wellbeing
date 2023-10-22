package com.awda.app.presentation.settings

import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.awda.app.common.Constants
import com.awda.app.common.isPermissionGranted
import com.awda.app.domain.service.BiometricAuthService
import com.awda.app.domain.service.WebLockerService
import com.awda.app.presentation.common.components.ErrorPopup
import com.awda.app.presentation.common.components.TimeLimitSlider
import com.awda.app.presentation.settings.components.AccessibilitySetting
import com.awda.app.presentation.settings.components.BlockedAppBottomSheet
import com.awda.app.presentation.settings.components.SecureAppBottomSheet
import com.awda.app.presentation.settings.components.SingleSettingItem
import com.awda.app.presentation.settings.components.Usage
import com.awda.app.presentation.settings.components.WebsitesBottomSheet

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, enableLockerService: (Boolean) -> Unit) {
    var showBlockedAppsBottomSheet by remember { mutableStateOf(false) }
    var showSecureAppsBottomSheet by remember { mutableStateOf(false) }
    var showWebsitesBottomSheet by remember { mutableStateOf(false) }
    var openTimePopup by remember { mutableStateOf(false) }
    var openErrorPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Usage(
            viewModel = viewModel,
            onGoalClick = {
                openTimePopup = true
            },
            onAlertSwitch = {
                if (it && !context.isPermissionGranted(Constants.OVERLAY_PERMISSION)) {
                    viewModel.setUsageAlert(false)
                    openErrorPopup = true
                } else {
                    viewModel.setUsageAlert(it)
                    enableLockerService(it)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SingleSettingItem(
            viewModel = viewModel,
            title = "Blocked Apps",
            onClick = {
                if (viewModel.usageAlertState.value && context.isPermissionGranted(Constants.OVERLAY_PERMISSION)) {
                    showBlockedAppsBottomSheet = true
                } else {
                    Toast
                        .makeText(
                            context,
                            "Enable alert notification to block apps",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AccessibilitySetting(
            name = WebLockerService::class.java.name,
            onClick = {
                showWebsitesBottomSheet = true
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AccessibilitySetting(
            name = BiometricAuthService::class.java.name,
            onClick = {
                showSecureAppsBottomSheet = true
            }
        )

        if (showWebsitesBottomSheet) {
            WebsitesBottomSheet(
                websites = viewModel.blockedWebsitesState.value,
                onChanged = {
                    viewModel.setBlockedWebsites(it)
                },
                onDismiss = {
                    showWebsitesBottomSheet = false
                }
            )
        }

        if (showBlockedAppsBottomSheet) {
            BlockedAppBottomSheet(
                apps = viewModel.blockedAppsState.value,
                onSelect = { selected, app ->
                    if (selected) {
                        viewModel.setBlockedApp(app)
                    } else {
                        viewModel.deleteBlockedApp(app)
                    }
                },
                onDismiss = {
                    showBlockedAppsBottomSheet = false
                },
                onChange = {
                    viewModel.setBlockedApp(it)
                }
            )
        }

        if (showSecureAppsBottomSheet) {
            SecureAppBottomSheet(
                apps = viewModel.secureAppsState.value,
                onSelect = { selected, app ->
                    if (selected) {
                        viewModel.setSecureApp(app)
                    } else {
                        viewModel.deleteSecureApp(app)
                    }
                },
                onDismiss = {
                    showSecureAppsBottomSheet = false
                }
            )
        }

        if (openTimePopup) {
            TimeLimitSlider(
                time = viewModel.usageTimeLimitState.value,
                onDismissRequest = {
                    openTimePopup = false
                    viewModel.setUsageTimeLimit(it)
                }
            )
        }

        if (openErrorPopup) {
            ErrorPopup(
                header = "Overlay Permission Not Granted",
                message = "Please grant overlay permission to show usage alerts.",
                permission = Constants.OVERLAY_PERMISSION
            ) {
                openErrorPopup = false
                viewModel.setUsageAlert(context.isPermissionGranted(Constants.OVERLAY_PERMISSION))
                enableLockerService(context.isPermissionGranted(Constants.OVERLAY_PERMISSION))
            }
        }
    }
}