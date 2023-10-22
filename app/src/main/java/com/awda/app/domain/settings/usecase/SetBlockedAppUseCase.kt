package com.awda.app.domain.settings.usecase

import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.usecase.AsyncUseCase

/**
 * Created by Abdelrahman Rizq
 */

class SetBlockedAppUseCase(private val repository: AppSettingsRepository) :
    AsyncUseCase<InstalledApp, Unit> {
    override suspend fun execute(params: InstalledApp) {
        return repository.setBlockedApp(params)
    }
}