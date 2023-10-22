package com.awda.app.domain.processors

import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.common.parseTimestampToClock
import com.awda.app.common.parseTimestampToDate
import com.awda.app.data.home.models.ChartEntry
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.data.home.models.TimelineNode
import com.awda.app.presentation.theme.DarkBlue
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

class ChartProcessor {

    fun createDayBasedEntriesAndTimeline(
        apps: List<CombinedAppUsage>,
        startTime: Long
    ): Pair<MutableList<ChartEntry>, MutableList<MutableList<TimelineNode>>> {
        val entries = mutableListOf<ChartEntry>()
        val timelines = mutableListOf<MutableList<TimelineNode>>()

        for (index in 0..48) {
            val currentStartTime = startTime + index * 30 * 60 * 1000
            val currentEndTime = currentStartTime + 30 * 60 * 1000

            var totalUsageTime = 0L
            val timeline = mutableListOf<TimelineNode>()

            apps.forEach { app ->
                app.timestamps.forEachIndexed { timestampIndex, timestamp ->
                    if (timestamp in currentStartTime..currentEndTime) {
                        totalUsageTime += app.usages[timestampIndex]
                        timeline.add(
                            TimelineNode(
                                app,
                                app.usages[timestampIndex],
                                timestamp,
                                currentStartTime
                            )
                        )
                    }
                }
            }

            val label = if (isDesiredLabelTime(currentStartTime)) {
                parseTimestampToClock(currentStartTime)
            } else {
                ""
            }

            entries.add(
                ChartEntry(
                    x = index.toFloat(),
                    y = (totalUsageTime.toFloat() / 600000f),
                    label = label
                )
            )

            timelines.add(timeline)
        }

        return Pair(entries, timelines)
    }

    fun createAppDayBasedEntries(
        app: CombinedAppUsage,
        startTime: Long
    ): MutableList<ChartEntry> {
        val entries = mutableListOf<ChartEntry>()
        for (index in 0..48) {
            val currentStartTime = startTime + index * 30 * 60 * 1000
            val currentEndTime = currentStartTime + 30 * 60 * 1000

            var totalUsageTime = 0L

            app.timestamps.forEachIndexed { i, timestamp ->
                if (timestamp in currentStartTime..currentEndTime) {
                    totalUsageTime += app.usages[i]
                }
            }

            val label = if (isDesiredLabelTime(currentStartTime)) {
                parseTimestampToClock(currentStartTime)
            } else {
                ""
            }

            entries.add(
                ChartEntry(
                    x = index.toFloat(),
                    y = (totalUsageTime.toFloat() / 600000f),
                    label = label
                )
            )
        }
        return entries
    }

    fun createWeekBasedEntries(
        apps: List<CombinedAppUsage>,
        startTime: Long
    ): MutableList<ChartEntry> {
        val entries = mutableListOf<ChartEntry>()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTime

        for (i in 0 until 7) {
            val dayStart = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            val dayEnd = calendar.timeInMillis

            var totalUsageTime = 0L
            apps.forEach { app ->
                app.timestamps.forEachIndexed { index, timestamp ->
                    if (timestamp in dayStart..dayEnd) {
                        totalUsageTime += app.usages[index]
                    }
                }
            }

            entries.add(
                ChartEntry(
                    x = i.toFloat(),
                    y = (totalUsageTime.toFloat() / (2 * 3600000f)),
                    label = parseTimestampToDate(dayStart)
                )
            )
        }

        return entries
    }

    fun createAppWeekBasedEntries(
        app: CombinedAppUsage,
        startTime: Long
    ): MutableList<ChartEntry> {
        val entries = mutableListOf<ChartEntry>()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTime

        for (i in 0 until 7) {
            val dayStart = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            val dayEnd = calendar.timeInMillis

            var totalUsageTime = 0L
            app.timestamps.forEachIndexed { index, timestamp ->
                if (timestamp in dayStart..dayEnd) {
                    totalUsageTime += app.usages[index]
                }
            }

            entries.add(
                ChartEntry(
                    x = i.toFloat(),
                    y = (totalUsageTime.toFloat() / (2 * 3600000f)),
                    label = parseTimestampToDate(dayStart)
                )
            )
        }

        return entries
    }

    fun createMonthBasedEntries(
        apps: List<CombinedAppUsage>,
        startTime: Long
    ): MutableList<ChartEntry> {
        val entries = mutableListOf<ChartEntry>()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTime

        for (i in 0 until 4) {
            val weekStart = calendar.timeInMillis
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
            val weekEnd = calendar.timeInMillis

            var totalUsageTime = 0L
            apps.forEach { app ->
                app.timestamps.forEachIndexed { index, timestamp ->
                    if (timestamp in weekStart..weekEnd) {
                        totalUsageTime += app.usages[index]
                    }
                }
            }

            entries.add(
                ChartEntry(
                    x = i.toFloat(),
                    y = (totalUsageTime.toFloat() / (14 * 3600000f)),
                    label = "Week ${i + 1}"
                )
            )
        }

        return entries
    }

    fun createAppMonthBasedEntries(
        app: CombinedAppUsage,
        startTime: Long
    ): MutableList<ChartEntry> {
        val entries = mutableListOf<ChartEntry>()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTime

        for (i in 0 until 4) {
            val weekStart = calendar.timeInMillis
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
            val weekEnd = calendar.timeInMillis

            var totalUsageTime = 0L
            app.timestamps.forEachIndexed { index, timestamp ->
                if (timestamp in weekStart..weekEnd) {
                    totalUsageTime += app.usages[index]
                }
            }

            entries.add(
                ChartEntry(
                    x = i.toFloat(),
                    y = (totalUsageTime.toFloat() / (14 * 3600000f)),
                    label = "Week ${i + 1}"
                )
            )
        }

        return entries
    }

    fun createData(entries: MutableList<ChartEntry>): ArrayList<BarData> {
        val list = arrayListOf<BarData>()
        entries.forEach {
            val point = Point(
                it.x,
                it.y
            )

            list.add(
                BarData(
                    point = point,
                    color = DarkBlue,
                    label = it.label
                )
            )
        }
        return list
    }

    private fun isDesiredLabelTime(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour % 4 == 0 && calendar.get(Calendar.MINUTE) == 0
    }
}