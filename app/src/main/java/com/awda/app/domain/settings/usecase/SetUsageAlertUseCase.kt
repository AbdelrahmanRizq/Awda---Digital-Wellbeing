package com.awda.app.domain.settings.usecase

import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.usecase.UseCase

/**
 * Created by Abdelrahman Rizq
 */

class SetUsageAlertUseCase(private val repository: AppSettingsRepository) : UseCase<Boolean, Unit> {
    override fun execute(params: Boolean) {
        return repository.setUsageAlert(params)
    }
}