package com.awda.app.data.intro

import com.awda.app.data.common.repository.SettingsRepository
import com.awda.app.domain.processors.PreferencesProcessor

/**
 * Created by Abdelrahman Rizq
 */

class AppIntroRepository(private val processor: PreferencesProcessor) :
    SettingsRepository(processor) {
    fun setAppIntroCompleted() {
        processor.setAppIntroCompleted()
    }

    fun getAppIntroCompleted() = processor.getAppIntroCompleted()
}