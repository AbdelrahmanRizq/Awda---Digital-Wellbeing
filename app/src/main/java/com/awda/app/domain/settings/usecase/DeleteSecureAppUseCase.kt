package com.awda.app.domain.settings.usecase

import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.data.settings.models.SecureApp
import com.awda.app.domain.common.usecase.AsyncUseCase

/**
 * Created by Abdelrahman Rizq
 */

class DeleteSecureAppUseCase(private val repository: AppSettingsRepository) :
    AsyncUseCase<SecureApp, Unit> {
    override suspend fun execute(params: SecureApp) {
        return repository.deleteSecureApp(params)
    }
}