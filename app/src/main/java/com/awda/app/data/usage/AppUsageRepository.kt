package com.awda.app.data.usage

import android.content.Context
import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.common.dayStart
import com.awda.app.data.common.models.Resource
import com.awda.app.data.common.repository.UsageRepository
import com.awda.app.data.home.db.AppUsageDao
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.processors.AppUsageProcessor
import com.awda.app.domain.processors.ChartProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

class AppUsageRepository(
    context: Context,
    processor: AppUsageProcessor,
    dao: AppUsageDao,
    private val chart: ChartProcessor
) : UsageRepository(context, dao, processor) {

    suspend fun getAppWeekUsage(packageName: String) = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startTime = calendar.timeInMillis

        when (val response = getAppUsage(packageName, TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                emit(Resource.Success(response.data!!))
            }
        }
    }

    suspend fun getAppMonthUsage(packageName: String) = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startTime = calendar.timeInMillis

        when (val response = getAppUsage(packageName, TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                emit(Resource.Success(response.data!!))
            }
        }
    }

    suspend fun getAppWeekAverageUsagePerDay(
        packageName: String
    ) = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startTime = calendar.timeInMillis

        when (val response = getAppUsage(packageName, TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                emit(Resource.Success(response.data!!.totalUsage / 7))
            }
        }
    }

    suspend fun getAppMonthAverageUsagePerDay(
        packageName: String
    ) = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startTime = calendar.timeInMillis

        when (val response = getAppUsage(packageName, TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                emit(Resource.Success(response.data!!.totalUsage / 30))
            }
        }
    }

    suspend fun getAppUsageChartData(
        packageName: String,
        base: ChartTimeBase
    ): Flow<Resource<List<BarData>>> = flow {
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

        when (val response = getAppUsage(packageName, TimeRange(startTime, endTime)).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                when (base) {
                    ChartTimeBase.DAY -> {
                        emit(
                            Resource.Success(
                                chart.createData(
                                    chart.createAppDayBasedEntries(
                                        response.data!!,
                                        startTime
                                    )
                                )
                            )
                        )
                    }

                    ChartTimeBase.WEEK -> {
                        emit(
                            Resource.Success(
                                chart.createData(
                                    chart.createAppWeekBasedEntries(
                                        response.data!!,
                                        startTime
                                    )
                                )
                            )
                        )
                    }

                    ChartTimeBase.MONTH -> {
                        emit(
                            Resource.Success(
                                chart.createData(
                                    chart.createAppMonthBasedEntries(
                                        response.data!!,
                                        startTime
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}