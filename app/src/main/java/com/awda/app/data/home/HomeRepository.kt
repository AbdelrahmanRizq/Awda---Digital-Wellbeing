package com.awda.app.data.home

import android.content.Context
import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.common.Constants
import com.awda.app.common.dayStart
import com.awda.app.common.isPermissionGranted
import com.awda.app.data.common.models.AwdaError
import com.awda.app.data.common.models.ErrorTypes
import com.awda.app.data.common.models.Resource
import com.awda.app.data.common.repository.UsageRepository
import com.awda.app.data.home.db.AppUsageDao
import com.awda.app.data.home.models.AddictionLevel
import com.awda.app.data.home.models.AppUsage
import com.awda.app.data.home.models.TimelineNode
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.processors.AppUsageProcessor
import com.awda.app.domain.processors.ChartProcessor
import com.awda.app.domain.processors.PreferencesProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

class HomeRepository(
    private val context: Context,
    private val dao: AppUsageDao,
    private val appUsage: AppUsageProcessor,
    private val preferences: PreferencesProcessor,
    private val chart: ChartProcessor
) : UsageRepository(context, dao, appUsage) {

    suspend fun getAddictionLevel(): Flow<Resource<AddictionLevel>> = flow {
        if (!context.isPermissionGranted(Constants.PACKAGE_USAGE_PERMISSION)) {
            emit(
                Resource.Error(
                    error = AwdaError(
                        type = ErrorTypes.PACKAGE_USAGE_PERMISSION_NOT_GRANTED,
                        message = "Permission not granted",
                        permission = Constants.PACKAGE_USAGE_PERMISSION
                    )
                )
            )
        }

        when (val response = getWeekAverageUsage().first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                when {
                    response.data!! > 5.5 * 60 * 60 * 1000 -> emit(Resource.Success(data = AddictionLevel.ADDICTED))
                    response.data > 4.5 * 60 * 60 * 1000 -> emit(Resource.Success(data = AddictionLevel.OBSESSED))
                    response.data > 3.5 * 60 * 60 * 1000 -> emit(Resource.Success(data = AddictionLevel.DEPENDENT))
                    response.data > 2.5 * 60 * 60 * 1000 -> emit(Resource.Success(data = AddictionLevel.HABITUAL))
                    response.data > 1 * 60 * 60 * 1000 -> emit(Resource.Success(data = AddictionLevel.ACHIEVER))
                    else -> emit(Resource.Success(data = AddictionLevel.CHAMPION))
                }
            }
        }
    }

    suspend fun getUsageChartData(base: ChartTimeBase):
            Flow<Pair<Resource<List<BarData>>, MutableList<MutableList<TimelineNode>>?>> = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        val startTime = when (base) {
            ChartTimeBase.DAY -> {
                dayStart().timeInMillis
            }

            ChartTimeBase.WEEK -> {
                calendar.add(Calendar.DAY_OF_WEEK, -7)
                calendar.timeInMillis
            }

            ChartTimeBase.MONTH -> {
                calendar.add(Calendar.DAY_OF_MONTH, -30)
                calendar.timeInMillis
            }
        }

        when (val response = getAppUsage(TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Pair(Resource.Error(response.error!!), null))
            }

            is Resource.Success -> {
                when (base) {
                    ChartTimeBase.DAY -> {
                        val (entries, timeline) = chart.createDayBasedEntriesAndTimeline(
                            response.data!!,
                            startTime
                        )
                        emit(
                            Pair(
                                Resource.Success(
                                    chart.createData(
                                        entries
                                    )
                                ),
                                timeline
                            )
                        )
                    }

                    ChartTimeBase.WEEK -> {
                        emit(
                            Pair(
                                Resource.Success(
                                    chart.createData(
                                        chart.createWeekBasedEntries(
                                            response.data!!,
                                            startTime
                                        )
                                    )
                                ),
                                null
                            )
                        )
                    }

                    ChartTimeBase.MONTH -> {
                        emit(
                            Pair(
                                Resource.Success(
                                    chart.createData(
                                        chart.createMonthBasedEntries(
                                            response.data!!,
                                            startTime
                                        )
                                    )
                                ),
                                null
                            )
                        )
                    }
                }

            }
        }
    }

    suspend fun getWeekUsage() = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startTime = calendar.timeInMillis

        when (val response = getAppUsage(TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                emit(Resource.Success(response.data!!))
            }
        }
    }

    suspend fun getWeekAverageUsage() = flow {
        when (val response = getWeekUsage().first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                val usageTime = response.data!!.sumOf { it.totalUsage }
                emit(Resource.Success(usageTime / 7L))
            }
        }
    }

    suspend fun getMonthAverageUsage() = flow {
        when (val response = retrieveMonthUsageWithFallback().first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                val usageTime = response.data!!.sumOf { it.totalUsage }
                emit(Resource.Success(usageTime / 30L))
            }
        }
    }

    suspend fun insertUsage(appUsage: List<AppUsage>) {
        withContext(Dispatchers.IO) {
            dao.insert(appUsage)
        }
    }

    fun getUsageTimeLimit() = preferences.getUsageTimeLimit()
}