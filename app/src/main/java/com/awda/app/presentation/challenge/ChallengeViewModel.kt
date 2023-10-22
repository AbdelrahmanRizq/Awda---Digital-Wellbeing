package com.awda.app.presentation.challenge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.common.models.Resource
import com.awda.app.domain.challenge.usecase.EnableChallengeUseCase
import com.awda.app.domain.challenge.usecase.InstalledAppsUseCase
import com.awda.app.domain.challenge.usecase.RetrieveChallengesUseCase
import com.awda.app.domain.challenge.usecase.SetChallengeUseCase
import com.awda.app.domain.common.models.EnableChallenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Abdelrahman Rizq
 */

class ChallengeViewModel(
    private val installedAppsUseCase: InstalledAppsUseCase,
    private val setChallengeUseCase: SetChallengeUseCase,
    private val retrieveChallengesUseCase: RetrieveChallengesUseCase,
    private val enableChallengeUseCase: EnableChallengeUseCase
) : ViewModel() {

    private val _appsState = mutableStateOf(mutableListOf<InstalledApp>())
    val appsState: State<MutableList<InstalledApp>> = _appsState

    private val _challengeState = mutableStateOf(listOf<AppChallenge>())
    val challengeState: State<List<AppChallenge>> = _challengeState

    init {
        getInstalledApps()
    }

    fun getInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = installedAppsUseCase.execute(Unit)) {
                is Resource.Error -> {
                    //Handle Error
                }

                is Resource.Success -> {
                    _appsState.value = response.data!!.toMutableList()
                }
            }
        }
    }

    fun getChallenges() {
        viewModelScope.launch(Dispatchers.IO) {
            retrieveChallengesUseCase.execute(Unit).collect {
                _challengeState.value = it.sortedByDescending { it.occurrence }
            }
        }
    }

    fun setChallenge(challenge: AppChallenge) {
        viewModelScope.launch(Dispatchers.IO) {
            setChallengeUseCase.execute(challenge)
        }
    }

    fun enableChallenge(challenge: AppChallenge, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            enableChallengeUseCase.execute(EnableChallenge(challenge, enabled))
        }
    }

    fun selectApp(app: InstalledApp) {
        if (_appsState.value.indexOf(app) > 8) {
            _appsState.value.remove(app)
            _appsState.value = _appsState.value.map {
                it.copy(selected = false)
            }.toMutableList()
            _appsState.value.add(7, app.copy(selected = true))
        } else {
            _appsState.value = _appsState.value.map {
                if (it.pName == app.pName) {
                    it.copy(selected = true)
                } else {
                    it.copy(selected = false)
                }
            }.toMutableList()
        }
    }

    fun setApps(apps: MutableList<InstalledApp>) {
        _appsState.value = apps
    }
}