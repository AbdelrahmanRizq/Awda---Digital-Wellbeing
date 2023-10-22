package com.awda.app.domain.common.usecase

import com.awda.app.data.common.repository.SettingsRepository

/**
 * Created by Abdelrahman Rizq
 */

class GetUsageTimeLimitUseCase(private val repository: SettingsRepository) : UseCase<Unit, Long> {
    override fun execute(params: Unit): Long {
        return repository.getUsageTimeLimit()
    }
}