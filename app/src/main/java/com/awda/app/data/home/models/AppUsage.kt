package com.awda.app.data.home.models

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by Abdelrahman Rizq
 */

@Entity(tableName = "app_usage")
data class AppUsage(
    @PrimaryKey var key: String = "",
    @ColumnInfo(name = "pName") var pName: String = "",
    @ColumnInfo(name = "name") var name: String? = null,
    @Ignore var icon: Drawable? = null,
    @ColumnInfo(name = "usage") var usage: Long = 0L,
    @ColumnInfo(name = "timestamp") var timestamp: Long = 0L
)

data class CombinedAppUsage(
    var keys: MutableList<String>,
    var pName: String,
    var name: String?,
    var icon: Drawable? = null,
    var totalUsage: Long,
    var usages: MutableList<Long>,
    var timestamps: MutableList<Long>
)

fun List<AppUsage>.combined(): MutableList<CombinedAppUsage> {
    val combinedAppUsage = mutableListOf<CombinedAppUsage>()

    groupBy {
        it.pName
    }.forEach {
        val app = CombinedAppUsage(
            keys = mutableListOf(),
            pName = it.value.first().pName,
            name = it.value.first().name,
            icon = it.value.first().icon,
            totalUsage = it.value.sumOf { it.usage },
            usages = mutableListOf(),
            timestamps = mutableListOf()
        )

        it.value.forEach {
            app.timestamps.add(it.timestamp)
            app.usages.add(it.usage)
            app.keys.add(it.key)
        }

        combinedAppUsage.add(app)
    }

    return combinedAppUsage.sortedByDescending { it.totalUsage }.toMutableList()
}

fun List<CombinedAppUsage>.separated(): MutableList<AppUsage> {
    val appUsages = mutableListOf<AppUsage>()

    forEach {
        it.timestamps.forEachIndexed { index, timestamp ->
            appUsages.add(
                AppUsage(
                    key = it.keys[index],
                    pName = it.pName,
                    name = it.name,
                    icon = it.icon,
                    usage = it.usages[index],
                    timestamp = timestamp
                )
            )
        }
    }

    return appUsages.sortedByDescending { it.usage }.toMutableList()
}