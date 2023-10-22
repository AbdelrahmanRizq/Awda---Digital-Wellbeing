package com.awda.app.domain.settings.usecase

import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.usecase.UseCase

/**
 * Created by Abdelrahman Rizq
 */

class GetBlockedWebsitesUseCase(private val repository: AppSettingsRepository) :
    UseCase<Unit, List<String>> {
    override fun execute(params: Unit): List<String> {
        return repository.getBlockedWebsites()
    }
}