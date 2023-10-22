package com.awda.app.domain.settings.usecase

import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.common.models.Resource
import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class InstalledAppsUseCase(private val repository: AppSettingsRepository) :
    AsyncUseCase<Unit, Resource<List<InstalledApp>>> {
    override suspend fun execute(params: Unit): Resource<List<InstalledApp>> {
        return repository.getInstalledApps().first()
    }
}