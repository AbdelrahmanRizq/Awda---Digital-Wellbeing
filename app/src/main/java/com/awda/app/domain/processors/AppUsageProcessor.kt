package com.awda.app.domain.processors

import android.app.AlarmManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.content.res.AppCompatResources
import com.awda.app.R
import com.awda.app.common.getAppIcon
import com.awda.app.common.getAppName
import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.home.models.AppUsage
import com.awda.app.data.home.models.combined
import com.awda.app.domain.common.models.TimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

class AppUsageProcessor(
    private val context: Context,
    private val usageStatsManager: UsageStatsManager
) {
    suspend fun query(
        range: TimeRange,
        loadIcons: Boolean = true
    ) = withContext(Dispatchers.IO) {
        result(events(range), loadIcons)
    }

    suspend fun runningApp() = withContext(Dispatchers.IO) {
        usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1),
            System.currentTimeMillis()
        )?.filterNotNull()?.maxByOrNull { it.lastTimeUsed }?.packageName
    }


    suspend fun sortedInstalledApps() = withContext(Dispatchers.IO) {
        val usages = query(
            range = TimeRange(
                System.currentTimeMillis() - 7 * AlarmManager.INTERVAL_DAY,
                System.currentTimeMillis()
            ),
            loadIcons = false
        ).combined()

        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        return@withContext packageManager.queryIntentActivities(
            intent,
            PackageManager.GET_META_DATA
        ).map {
            InstalledApp(
                pName = it.activityInfo.packageName,
                name = it.loadLabel(packageManager).toString(),
                icon = it.activityInfo.loadIcon(packageManager)
            )
        }.sortedByDescending { app ->
            usages.find { it.pName == app.pName }?.totalUsage ?: 0
        }.distinctBy { it.pName }
    }

    private fun events(range: TimeRange): ArrayList<UsageEvents.Event> {
        val events = ArrayList<UsageEvents.Event>()
        val usageEvents = usageStatsManager.queryEvents(range.start, range.end)
        while (usageEvents.hasNextEvent()) {
            val currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND ||
                currentEvent.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND
            ) {
                events.add(currentEvent)
            }
        }
        return events
    }

    private fun result(
        events: ArrayList<UsageEvents.Event>,
        loadIcons: Boolean
    ): List<AppUsage> {
        val appUsages = mutableListOf<AppUsage>()
        for (i in 0 until events.size - 1) {
            val currentEvent = events[i]
            val nextEvent = events[i + 1]
            if (currentEvent.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND && nextEvent.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND && currentEvent.className == nextEvent.className) {
                val pName = nextEvent.packageName
                val usage = nextEvent.timeStamp - currentEvent.timeStamp

                val app = appUsages.firstOrNull { it.pName == pName }
                val name = app?.name
                val icon = app?.icon

                appUsages.add(
                    AppUsage(
                        key = "$pName${nextEvent.timeStamp}$usage",
                        pName = pName,
                        name = name ?: context.getAppName(pName),
                        icon = if (loadIcons) icon ?: context.getAppIcon(pName)
                        ?: AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_unavailable
                        ) else null,
                        usage = usage,
                        timestamp = nextEvent.timeStamp
                    )
                )
            }
        }

        return appUsages.filterNot {
            it.pName == context.packageName || it.pName.contains("launcher")
        }.sortedByDescending {
            it.usage
        }
    }
}
