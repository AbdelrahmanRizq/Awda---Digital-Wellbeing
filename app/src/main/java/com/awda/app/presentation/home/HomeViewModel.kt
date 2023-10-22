package com.awda.app.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.common.dayStart
import com.awda.app.data.common.models.AwdaError
import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.models.AddictionLevel
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.data.home.models.TimelineNode
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.home.usecase.AddictionLevelUseCase
import com.awda.app.domain.home.usecase.AppUsageUseCase
import com.awda.app.domain.home.usecase.MonthAverageUsageUseCase
import com.awda.app.domain.home.usecase.UsageChartUseCase
import com.awda.app.domain.home.usecase.UsageProgressUseCase
import com.awda.app.domain.home.usecase.WeekAverageUsageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

class HomeViewModel(
    private val appUsageUseCase: AppUsageUseCase,
    private val usageProgressUseCase: UsageProgressUseCase,
    private val addictionLevelUseCase: AddictionLevelUseCase,
    private val usageChartUseCase: UsageChartUseCase,
    private val weekAverageUsageUseCase: WeekAverageUsageUseCase,
    private val monthAverageUsageUseCase: MonthAverageUsageUseCase
) : ViewModel() {

    private val _usageTodayProgressState = mutableStateOf(Pair(-1f, "Unavailable"))
    val usageTodayProgressState: State<Pair<Float, String>> = _usageTodayProgressState

    private val _addictionLevelState = mutableStateOf(AddictionLevel.UNAVAILABLE)
    val addictionLevelState: State<AddictionLevel> = _addictionLevelState

    private val _averageWeekUsageState = mutableStateOf(0L)
    val averageWeekUsageState: State<Long> = _averageWeekUsageState

    private val _averageMonthUsageState = mutableStateOf(0L)
    val averageMonthUsageState: State<Long> = _averageMonthUsageState

    private val _errorState = mutableStateOf<AwdaError?>(null)
    val errorState: State<AwdaError?> = _errorState

    private val _appUsageState = mutableStateOf(listOf<CombinedAppUsage>())
    val appUsageState: State<List<CombinedAppUsage>> = _appUsageState

    private val _usageTimeTodayState = mutableStateOf(0L)
    val usageTimeTodayState: State<Long> = _usageTimeTodayState

    private val _usageTimeYesterdayState = mutableStateOf(0L)
    val usageTimeYesterdayState: State<Long> = _usageTimeYesterdayState

    private val _dayChartDataState = mutableStateOf<List<BarData>>(emptyList())
    val dayChartDataState: State<List<BarData>> = _dayChartDataState

    private val _weekChartDataState = mutableStateOf<List<BarData>>(emptyList())
    val weekChartDataState: State<List<BarData>> = _weekChartDataState

    private val _monthChartDataState = mutableStateOf<List<BarData>>(emptyList())
    val monthChartDataState: State<List<BarData>> = _monthChartDataState

    private val _timelineState = mutableStateOf<Map<Long, List<TimelineNode>>?>(null)
    val timelineState: State<Map<Long, List<TimelineNode>>?> = _timelineState

    private val _baseErrorState = mutableStateOf<AwdaError?>(null)
    val baseErrorState: State<AwdaError?> = _baseErrorState

    fun getTodayAppUsage(
        onComplete: (Long) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        val startTime = dayStart().timeInMillis
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = appUsageUseCase.execute(TimeRange(startTime, endTime))) {
                is Resource.Error -> {
                    _baseErrorState.value = response.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _usageTimeTodayState.value = response.data!!.sumOf { it.totalUsage }
                        _appUsageState.value = response.data
                        onComplete(_usageTimeTodayState.value)
                    }
                }
            }
        }
    }

    fun getYesterdayAppUsage() {
        val startOfDay = dayStart()
        val endTime = startOfDay.timeInMillis
        startOfDay.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = startOfDay.timeInMillis
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = appUsageUseCase.execute(TimeRange(startTime, endTime))) {
                is Resource.Error -> {
                    _baseErrorState.value = response.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _usageTimeYesterdayState.value = response.data!!.sumOf { it.totalUsage }
                    }
                }
            }
        }
    }

    fun getDayUsageChartData() {
        viewModelScope.launch(Dispatchers.IO) {
            val (chart, timeline) = usageChartUseCase.execute(ChartTimeBase.DAY)
            when (chart) {
                is Resource.Error -> {
                    _baseErrorState.value = chart.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _dayChartDataState.value = chart.data!!
                        if (timeline != null) {
                            _timelineState.value = timeline
                        }
                    }
                }
            }
        }
    }

    fun getWeekUsageChartData() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = usageChartUseCase.execute(ChartTimeBase.WEEK).first) {
                is Resource.Error -> {
                    _baseErrorState.value = response.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _weekChartDataState.value = response.data!!
                    }
                }
            }
        }
    }

    fun getMonthUsageChartData() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = usageChartUseCase.execute(ChartTimeBase.MONTH).first) {
                is Resource.Error -> {
                    _baseErrorState.value = response.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _monthChartDataState.value = response.data!!
                    }
                }
            }
        }
    }

    fun getTodayUsageProgress(usageTimeToday: Long) {
        _usageTodayProgressState.value = usageProgressUseCase.execute(usageTimeToday)
    }

    fun getAddictionLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = addictionLevelUseCase.execute(Unit)) {
                is Resource.Error -> {
                    _errorState.value = response.error!!

                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _addictionLevelState.value = response.data!!
                    }
                }
            }
        }
    }

    fun getWeekAverageUsage() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = weekAverageUsageUseCase.execute(Unit)) {
                is Resource.Error -> {
                    _errorState.value = response.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _averageWeekUsageState.value = response.data!!
                    }
                }
            }

        }
    }

    fun getMonthAverageUsage() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = monthAverageUsageUseCase.execute(Unit)) {
                is Resource.Error -> {
                    _errorState.value = response.error!!
                }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        _averageMonthUsageState.value = response.data!!
                    }
                }
            }
        }
    }

    fun clearError() {
        _errorState.value = null
    }
}