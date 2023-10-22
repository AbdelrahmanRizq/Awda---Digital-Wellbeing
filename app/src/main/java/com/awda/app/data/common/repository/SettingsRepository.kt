package com.awda.app.data.common.repository

import com.awda.app.domain.processors.PreferencesProcessor

/**
 * Created by Abdelrahman Rizq
 */

open class SettingsRepository(private val processor: PreferencesProcessor) {
    fun setUsageTimeLimit(time: Long) {
        processor.setUsageTimeLimit(time)
    }

    fun getUsageTimeLimit() = processor.getUsageTimeLimit()
}