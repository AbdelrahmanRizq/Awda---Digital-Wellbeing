package com.awda.app.domain.settings.usecase

import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.usecase.UseCase

/**
 * Created by Abdelrahman Rizq
 */

class SetBlockedWebsitesUseCase(private val repository: AppSettingsRepository) :
    UseCase<List<String>, Unit> {
    override fun execute(params: List<String>) {
        return repository.setBlockedWebsites(params)
    }
}