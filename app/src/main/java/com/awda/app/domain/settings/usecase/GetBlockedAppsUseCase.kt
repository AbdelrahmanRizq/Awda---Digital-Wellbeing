package com.awda.app.domain.settings.usecase

import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

class GetBlockedAppsUseCase(private val repository: AppSettingsRepository) :
    AsyncUseCase<Unit, Flow<List<InstalledApp>>> {
    override suspend fun execute(params: Unit): Flow<List<InstalledApp>> {
        return repository.getBlockedApps()
    }
}