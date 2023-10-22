package com.awda.app.domain.common.usecase

import com.awda.app.data.common.repository.SettingsRepository

/**
 * Created by Abdelrahman Rizq
 */

class SetUsageTimeLimitUseCase(private val repository: SettingsRepository) : UseCase<Long, Unit> {
    override fun execute(params: Long) {
        return repository.setUsageTimeLimit(params)
    }
}