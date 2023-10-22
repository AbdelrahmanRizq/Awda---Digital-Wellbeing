package com.awda.app.presentation.usage

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.data.common.models.AwdaError
import com.awda.app.data.common.models.ErrorTypes
import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.domain.usage.usecase.AppMonthAverageUsagePerDayUseCase
import com.awda.app.domain.usage.usecase.AppMonthTotalUsageUseCase
import com.awda.app.domain.usage.usecase.AppUsageChartUseCase
import com.awda.app.domain.usage.usecase.AppWeekAverageUsagePerDayUseCase
import com.awda.app.domain.usage.usecase.AppWeekTotalUsageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Abdelrahman Rizq
 */

class AppUsageViewModel(
    private val appWeekAverageUsagePerDayUseCase: AppWeekAverageUsagePerDayUseCase,
    private val appMonthAverageUsagePerDayUseCase: AppMonthAverageUsagePerDayUseCase,
    private val appWeekTotalUsageUseCase: AppWeekTotalUsageUseCase,
    private val appMonthTotalUsageUseCase: AppMonthTotalUsageUseCase,
    private val appUsageChartUseCase: AppUsageChartUseCase
) : ViewModel() {

    private val _appUsageState = mutableStateOf(listOf<CombinedAppUsage>())
    val appUsageState: State<List<CombinedAppUsage>> = _appUsageState

    private val _weekAverageAppUsagePerDayState = mutableStateOf(0L)
    val weekAverageAppUsagePerDayState: State<Long> = _weekAverageAppUsagePerDayState

    private val _monthAverageAppUsagePerDayState = mutableStateOf(0L)
    val monthAverageAppUsagePerDayState: State<Long> = _monthAverageAppUsagePerDayState

    private val _weekTotalAppUsageState = mutableStateOf(0L)
    val weekTotalAppUsageState: State<Long> = _weekTotalAppUsageState

    private val _monthTotalAppUsageState = mutableStateOf(0L)
    val monthTotalAppUsageState: State<Long> = _monthTotalAppUsageState

    private val _dayAppChartDataState = mutableStateOf<List<BarData>>(emptyList())
    val dayAppChartDataState: State<List<BarData>> = _dayAppChartDataState

    private val _weekAppChartDataState = mutableStateOf<List<BarData>>(emptyList())
    val weekAppChartDataState: State<List<BarData>> = _weekAppChartDataState

    private val _monthAppChartDataState = mutableStateOf<List<BarData>>(emptyList())
    val monthAppChartDataState: State<List<BarData>> = _monthAppChartDataState

    private val _errorState = mutableStateOf<AwdaError?>(null)
    val errorState: State<AwdaError?> = _errorState

    private val _selectedApp = mutableStateOf<CombinedAppUsage?>(null)

    fun getDayAppUsageChartData() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = appUsageChartUseCase.execute(
                    Pair(_selectedApp.value!!.pName, ChartTimeBase.DAY)
                )) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _dayAppChartDataState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun getWeekAppUsageChartData() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = appUsageChartUseCase.execute(
                    Pair(_selectedApp.value!!.pName, ChartTimeBase.WEEK)
                )) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _weekAppChartDataState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun getMonthAppUsageChartData() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = appUsageChartUseCase.execute(
                    Pair(_selectedApp.value!!.pName, ChartTimeBase.MONTH)
                )) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _monthAppChartDataState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun getWeekTotalAppUsage() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response =
                    appWeekTotalUsageUseCase.execute(_selectedApp.value!!.pName)) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _weekTotalAppUsageState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun getMonthTotalAppUsage() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response =
                    appMonthTotalUsageUseCase.execute(_selectedApp.value!!.pName)) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _monthTotalAppUsageState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun getWeekAverageAppUsagePerDay() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response =
                    appWeekAverageUsagePerDayUseCase.execute(_selectedApp.value!!.pName)) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _weekAverageAppUsagePerDayState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun getMonthAverageAppUsagePerDay() {
        if (_selectedApp.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response =
                    appMonthAverageUsagePerDayUseCase.execute(_selectedApp.value!!.pName)) {
                    is Resource.Error -> {
                        _errorState.value = response.error!!
                    }

                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _monthAverageAppUsagePerDayState.value = response.data!!
                        }
                    }
                }
            }
        } else {
            _errorState.value =
                AwdaError(type = ErrorTypes.UNKNOWN_ERROR, message = "Unknown Error")
        }
    }

    fun selectApp(appUsage: CombinedAppUsage) {
        _selectedApp.value = appUsage
    }

    fun setAppUsage(appUsage: List<CombinedAppUsage>) {
        _appUsageState.value = appUsage
    }

    fun clearError() {
        _errorState.value = null
    }
}