package com.awda.app.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.HomeRepository
import com.awda.app.data.home.models.separated
import kotlinx.coroutines.flow.first
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by Abdelrahman Rizq
 */

class AppUsageWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val repository: HomeRepository by inject(HomeRepository::class.java)

    override suspend fun doWork(): Result {
        return when (val response = repository.getWeekUsage().first()) {
            is Resource.Error -> {
                Result.failure()
            }

            is Resource.Success -> {
                repository.insertUsage(response.data!!.separated())
                Result.success()
            }
        }
    }
}