package com.awda.app.data.common.repository

import android.content.Context
import com.awda.app.common.Constants
import com.awda.app.common.getAppIcon
import com.awda.app.common.isPermissionGranted
import com.awda.app.data.common.models.AwdaError
import com.awda.app.data.common.models.ErrorTypes
import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.db.AppUsageDao
import com.awda.app.data.home.models.AppUsage
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.data.home.models.combined
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.processors.AppUsageProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

open class UsageRepository(
    private val context: Context,
    private val dao: AppUsageDao,
    private val processor: AppUsageProcessor,
) {

    suspend fun getAppUsage(
        range: TimeRange,
        useCache: Boolean = true
    ): Flow<Resource<List<CombinedAppUsage>>> = flow {
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
            return@flow
        }

        if (range.requiresCache() && useCache) {
            when (val response = retrieveMonthUsageWithFallback().first()) {
                is Resource.Error -> {
                    emit(Resource.Error(response.error!!))
                }

                is Resource.Success -> {
                    emit(Resource.Success(data = response.data!!))
                }
            }
        } else {
            emit(Resource.Success(data = processor.query(range).combined()))
        }
    }

    suspend fun getAppUsage(
        packageName: String,
        range: TimeRange,
        useCache: Boolean = true
    ): Flow<Resource<CombinedAppUsage>> = flow {
        when (val response = getAppUsage(range, useCache).first()) {
            is Resource.Error -> {
                emit(Resource.Error(response.error!!))
            }

            is Resource.Success -> {
                emit(Resource.Success(response.data!!.first { it.pName == packageName }))
            }
        }
    }

    suspend fun retrieveMonthUsageWithFallback() = flow {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val lastWeek = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -23)
        val startTime = calendar.timeInMillis

        suspend fun retrieveMonthUsage(): List<AppUsage> {
            val localCalendar = Calendar.getInstance()
            val currentTimestamp = localCalendar.timeInMillis
            localCalendar.add(Calendar.DAY_OF_YEAR, -30)
            val lastMonthTimestamp = localCalendar.timeInMillis

            return retrieveAppUsages().first().filter {
                it.timestamp in lastMonthTimestamp..currentTimestamp
            }
        }

        if (retrieveAppUsages().first().any { it.timestamp in startTime..lastWeek }) {
            emit(Resource.Success(retrieveMonthUsage().combined()))
        } else {
            when (val response = getAppUsage(TimeRange(startTime, endTime), false).first()) {
                is Resource.Error -> {
                    emit(Resource.Error(response.error!!))
                }

                is Resource.Success -> {
                    emit(Resource.Success(response.data!!))
                }
            }
        }
    }

    private suspend fun retrieveAppUsages() = flow {
        fun withIcons(usage: List<AppUsage>): List<AppUsage> {
            val apps = mutableListOf<AppUsage>()
            usage.forEach { item ->
                val existingApp = apps.firstOrNull { it.pName == item.pName }
                val icon = existingApp?.icon ?: context.getAppIcon(item.pName)
                val app = item.copy(icon = icon)
                apps.add(app)
            }

            return apps
        }
        emit(withIcons(dao.retrieve().first()))
    }
}