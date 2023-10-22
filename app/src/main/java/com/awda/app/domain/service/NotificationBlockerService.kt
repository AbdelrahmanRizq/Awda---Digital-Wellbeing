package com.awda.app.domain.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.awda.app.common.parseMillisToClock
import com.awda.app.data.settings.AppSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

class NotificationBlockerService : NotificationListenerService() {
    private val repository: AppSettingsRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        scope.launch(Dispatchers.IO) {
            val blockedApp = repository.getBlockedApps().first()
                .find { it.pName == sbn.packageName && it.block.blockNotifications }
            val calendar = Calendar.getInstance()
            if (blockedApp != null) {
                val (hours, minutes) = parseMillisToClock(blockedApp.block.startTimeOfDay)
                calendar.set(
                    Calendar.HOUR_OF_DAY,
                    hours
                )
                calendar.set(
                    Calendar.MINUTE,
                    minutes
                )
            }

            if (blockedApp != null && System.currentTimeMillis() in calendar.timeInMillis..calendar.timeInMillis + blockedApp.block.duration) {
                cancelNotification(sbn.key)
            } else {
                super.onNotificationPosted(sbn)
            }
        }
    }
}