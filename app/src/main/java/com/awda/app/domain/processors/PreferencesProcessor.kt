package com.awda.app.domain.processors

import android.content.SharedPreferences
import com.awda.app.common.Constants

/**
 * Created by Abdelrahman Rizq
 */

class PreferencesProcessor(private val preferences: SharedPreferences) {
    fun setAppIntroCompleted() {
        preferences.edit().putBoolean(Constants.APP_INTRO_COMPLETED, true).apply()
    }

    fun getAppIntroCompleted() = preferences.getBoolean(Constants.APP_INTRO_COMPLETED, false)

    fun setUsageTimeLimit(time: Long) {
        preferences.edit().putLong(Constants.USAGE_TIME_LIMIT, time).apply()
    }

    fun getUsageTimeLimit() =
        preferences.getLong(Constants.USAGE_TIME_LIMIT, Constants.DEFAULT_TIME_USAGE)

    fun setUsageAlert(enabled: Boolean) {
        preferences.edit().putBoolean(Constants.USAGE_ALERT, enabled).apply()
    }

    fun getUsageAlert() = preferences.getBoolean(Constants.USAGE_ALERT, true)

    fun setBlockedWebsites(apps: List<String>) {
        preferences.edit().putStringSet(Constants.BLOCKED_WEBSITES, apps.toSet()).apply()
    }

    fun getBlockedWebsites() =
        preferences.getStringSet(Constants.BLOCKED_WEBSITES, setOf())!!.toList()

}