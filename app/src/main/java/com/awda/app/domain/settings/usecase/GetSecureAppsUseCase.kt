package com.awda.app.domain.settings.usecase

import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.data.settings.models.SecureApp
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

class GetSecureAppsUseCase(private val repository: AppSettingsRepository) :
    AsyncUseCase<Unit, Flow<List<SecureApp>>> {
    override suspend fun execute(params: Unit): Flow<List<SecureApp>> {
        return repository.getSecureApps()
    }
}