package com.awda.app.domain.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.awda.app.R
import com.awda.app.common.Constants
import com.awda.app.common.dayStart
import com.awda.app.common.isPermissionGranted
import com.awda.app.common.parseMillisToClock
import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.processors.AppUsageProcessor
import com.awda.app.domain.processors.PreferencesProcessor
import com.awda.app.presentation.common.components.LockerDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

class AppLockerService : Service() {

    private val dialog: LockerDialog by inject()
    private val appUsage: AppUsageProcessor by inject()
    private val preferences: PreferencesProcessor by inject()
    private val repository: AppSettingsRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var handler: Handler

    override fun onCreate() {
        super.onCreate()
        trackEvents()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        job.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        foreground()
        return START_STICKY
    }

    private fun trackEvents() {
        val handlerThread = HandlerThread("AppLockerThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        trackScreenTime()
        trackBlockedApps()
    }

    private fun trackBlockedApps() {
        fun blocked(app: InstalledApp): Boolean {
            val calendar = Calendar.getInstance()
            val (hours, minutes) = parseMillisToClock(app.block.startTimeOfDay)
            calendar.set(
                Calendar.HOUR_OF_DAY,
                hours
            )
            calendar.set(
                Calendar.MINUTE,
                minutes
            )

            return System.currentTimeMillis() in calendar.timeInMillis..calendar.timeInMillis + app.block.duration &&
                    app.block.days.any { it.value == calendar.get(Calendar.DAY_OF_WEEK) }
        }

        fun check() {
            if (!isPermissionGranted(Constants.OVERLAY_PERMISSION)) {
                stopSelf()
                return
            }
            scope.launch(Dispatchers.IO) {
                val app =
                    repository.getBlockedApps().first().find { it.pName == appUsage.runningApp() }
                if (app != null && blocked(app)) {
                    withContext(Dispatchers.Main) {
                        dialog.show(locker = LockerDialog.Type.BLOCKED_APP)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        dialog.dismiss(locker = LockerDialog.Type.BLOCKED_APP)
                    }
                }
            }
        }

        handler.post(
            object : Runnable {
                override fun run() {
                    check()
                    handler.postDelayed(this, 1000)
                }
            }
        )
    }

    private fun trackScreenTime() {
        var allowExtra = false

        var extraRequestTimestamp = 0L

        fun expired(limit: Long, usage: Long) =
            (usage > limit) && !allowExtra

        fun extraExpired() =
            System.currentTimeMillis() >= (extraRequestTimestamp + TimeUnit.MINUTES.toMillis(5))

        fun check() {
            if (!isPermissionGranted(Constants.OVERLAY_PERMISSION)) {
                stopSelf()
                return
            }
            scope.launch(Dispatchers.IO) {
                val calendar = Calendar.getInstance()
                val endTime = calendar.timeInMillis
                val startTime = dayStart().timeInMillis
                val usage = appUsage.query(range = TimeRange(startTime, endTime), loadIcons = false)
                    .sumOf { it.usage }
                if (expired(preferences.getUsageTimeLimit(), usage)) {
                    withContext(Dispatchers.Main) {
                        dialog.show(locker = LockerDialog.Type.SCREEN_TIME) {
                            extraRequestTimestamp = System.currentTimeMillis()
                            allowExtra = true
                        }
                    }
                }

                if (extraExpired()) {
                    allowExtra = false
                }
            }
        }

        handler.post(
            object : Runnable {
                override fun run() {
                    check()
                    handler.postDelayed(this, 10 * 1000)
                }
            }
        )
    }

    private fun foreground() {
        val channelId = "AppLockerChannel"
        val channelName = "Awda Locker"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Awda Usage Service")
            .setSmallIcon(R.drawable.ic_awda)
            .build()

        startForeground(1, notification)
    }
}
