package com.awda.app.presentation.intro

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.awda.app.common.Constants
import com.awda.app.domain.common.usecase.GetUsageTimeLimitUseCase
import com.awda.app.domain.common.usecase.SetUsageTimeLimitUseCase
import com.awda.app.domain.intro.usecase.GetAppIntroCompletedUseCase
import com.awda.app.domain.intro.usecase.SetAppIntroCompletedUseCase

/**
 * Created by Abdelrahman Rizq
 */

class AppIntroViewModel(
    private val setAppIntroCompletedUseCase: SetAppIntroCompletedUseCase,
    private val setUsageTimeLimitUseCase: SetUsageTimeLimitUseCase,
    private val getAppIntroCompletedUseCase: GetAppIntroCompletedUseCase,
    private val getUsageTimeLimitUseCase: GetUsageTimeLimitUseCase
) : ViewModel() {

    private val _appIntroCompletionState = mutableStateOf(false)
    val appIntroCompletionState: State<Boolean> = _appIntroCompletionState

    private val _usageTimeLimitState = mutableStateOf(Constants.DEFAULT_TIME_USAGE)
    val usageTimeLimitState: State<Long> = _usageTimeLimitState

    init {
        getAppIntroCompleted()
        getUsageTimeLimit()
    }

    fun setAppIntroCompleted() {
        setAppIntroCompletedUseCase.execute(Unit)
        getAppIntroCompleted() // update state
    }

    fun setUsageTimeLimit(time: Long) {
        setUsageTimeLimitUseCase.execute(time)
        getUsageTimeLimit() //update state
    }

    private fun getAppIntroCompleted() {
        _appIntroCompletionState.value = getAppIntroCompletedUseCase.execute(Unit)
    }

    private fun getUsageTimeLimit() {
        _usageTimeLimitState.value = getUsageTimeLimitUseCase.execute(Unit)
    }
}