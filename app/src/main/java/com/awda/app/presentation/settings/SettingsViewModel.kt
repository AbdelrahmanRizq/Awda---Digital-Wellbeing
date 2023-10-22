package com.awda.app.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awda.app.common.Constants
import com.awda.app.data.challenge.models.BlockConfiguration
import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.common.models.Resource
import com.awda.app.data.settings.models.SecureApp
import com.awda.app.domain.common.usecase.GetUsageTimeLimitUseCase
import com.awda.app.domain.common.usecase.SetUsageTimeLimitUseCase
import com.awda.app.domain.settings.usecase.DeleteBlockedAppUseCase
import com.awda.app.domain.settings.usecase.DeleteSecureAppUseCase
import com.awda.app.domain.settings.usecase.GetBlockedAppsUseCase
import com.awda.app.domain.settings.usecase.GetBlockedWebsitesUseCase
import com.awda.app.domain.settings.usecase.GetSecureAppsUseCase
import com.awda.app.domain.settings.usecase.GetUsageAlertUseCase
import com.awda.app.domain.settings.usecase.InstalledAppsUseCase
import com.awda.app.domain.settings.usecase.SetBlockedAppUseCase
import com.awda.app.domain.settings.usecase.SetBlockedWebsitesUseCase
import com.awda.app.domain.settings.usecase.SetSecureAppUseCase
import com.awda.app.domain.settings.usecase.SetUsageAlertUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Abdelrahman Rizq
 */

class SettingsViewModel(
    private val setUsageTimeLimitUseCase: SetUsageTimeLimitUseCase,
    private val getUsageTimeLimitUseCase: GetUsageTimeLimitUseCase,
    private val setUsageAlertUseCase: SetUsageAlertUseCase,
    private val getUsageAlertUseCase: GetUsageAlertUseCase,
    private val installedAppsUseCase: InstalledAppsUseCase,
    private val setBlockedAppUseCase: SetBlockedAppUseCase,
    private val getBlockedAppsUseCase: GetBlockedAppsUseCase,
    private val deleteBlockedAppUseCase: DeleteBlockedAppUseCase,
    private val setBlockedWebsitesUseCase: SetBlockedWebsitesUseCase,
    private val getBlockedWebsitesUseCase: GetBlockedWebsitesUseCase,
    private val setSecureAppUseCase: SetSecureAppUseCase,
    private val getSecureAppsUseCase: GetSecureAppsUseCase,
    private val deleteSecureAppUseCase: DeleteSecureAppUseCase
) : ViewModel() {

    private val _usageTimeLimitState = mutableStateOf(Constants.DEFAULT_TIME_USAGE)
    val usageTimeLimitState: State<Long> = _usageTimeLimitState

    private val _usageAlertState = mutableStateOf(false)
    val usageAlertState: State<Boolean> = _usageAlertState

    private val _appsState = mutableStateOf(mutableListOf<InstalledApp>())

    private val _blockedAppsState = mutableStateOf(mutableListOf<InstalledApp>())
    val blockedAppsState: State<MutableList<InstalledApp>> = _blockedAppsState
    private val _blockedWebsitesState = mutableStateOf(mutableListOf<String>())
    val blockedWebsitesState: State<MutableList<String>> = _blockedWebsitesState

    private val _secureAppsState = mutableStateOf(mutableListOf<SecureApp>())
    val secureAppsState: State<MutableList<SecureApp>> = _secureAppsState


    init {
        getUsageTimeLimit()
        getUsageAlert()
        getInstalledApps {
            getBlockedApps()
            getSecureApps()
        }
        getBlockedWebsites()
    }

    fun setBlockedApp(app: InstalledApp) {
        viewModelScope.launch(Dispatchers.IO) {
            setBlockedAppUseCase.execute(app)
        }
    }

    fun deleteBlockedApp(app: InstalledApp) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBlockedAppUseCase.execute(app)
        }
    }

    fun setBlockedWebsites(apps: List<String>) {
        setBlockedWebsitesUseCase.execute(apps)
        getBlockedWebsites()
    }

    fun setSecureApp(app: SecureApp) {
        viewModelScope.launch(Dispatchers.IO) {
            setSecureAppUseCase.execute(app)
        }
    }

    fun deleteSecureApp(app: SecureApp) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSecureAppUseCase.execute(app)
        }
    }

    fun setUsageTimeLimit(time: Long) {
        setUsageTimeLimitUseCase.execute(time)
        getUsageTimeLimit() //update state
    }

    fun setUsageAlert(enabled: Boolean) {
        setUsageAlertUseCase.execute(enabled)
        getUsageAlert() //update state
    }

    private fun getUsageTimeLimit() {
        _usageTimeLimitState.value = getUsageTimeLimitUseCase.execute(Unit)
    }

    private fun getUsageAlert() {
        _usageAlertState.value = getUsageAlertUseCase.execute(Unit)
    }

    private fun getInstalledApps(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = installedAppsUseCase.execute(Unit)) {
                is Resource.Error -> {

                }

                is Resource.Success -> {
                    _appsState.value = response.data!!.toMutableList()
                    onComplete()
                }
            }
        }
    }

    private fun getBlockedApps() {
        viewModelScope.launch(Dispatchers.IO) {
            getBlockedAppsUseCase.execute(Unit).collect { blockedApps ->
                val result = mutableListOf<InstalledApp>()
                _appsState.value.forEach { app ->
                    val blockedApp = blockedApps.find { it.pName == app.pName }
                    if (blockedApp == null) {
                        result.add(app.copy(selected = false, block = BlockConfiguration()))
                    } else {
                        result.add(app.copy(selected = true, block = blockedApp.block))
                    }
                }
                _blockedAppsState.value = result
            }
        }
    }

    private fun getSecureApps() {
        viewModelScope.launch(Dispatchers.IO) {
            getSecureAppsUseCase.execute(Unit).collect { secureApps ->
                val result = mutableListOf<SecureApp>()
                _appsState.value.forEach { app ->
                    var secureApp = secureApps.find { it.info.pName == app.pName }
                    if (secureApp == null) {
                        secureApp = SecureApp()
                        secureApp.key = app.pName
                        secureApp.info = app
                        secureApp.info.selected = false
                    } else {
                        secureApp.key = app.pName
                        secureApp.info = app
                        secureApp.info.selected = true
                    }
                    result.add(secureApp)
                }
                _secureAppsState.value = result
            }
        }
    }

    private fun getBlockedWebsites() {
        _blockedWebsitesState.value = getBlockedWebsitesUseCase.execute(Unit).toMutableList()
    }
}