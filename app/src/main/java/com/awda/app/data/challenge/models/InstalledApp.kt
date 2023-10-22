package com.awda.app.data.challenge.models

import android.graphics.drawable.Drawable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.awda.app.data.challenge.converters.CustomRepetitionPatternConverter

/**
 * Created by Abdelrahman Rizq
 */

@Entity(tableName = "blocked_app")
data class InstalledApp(
    @PrimaryKey var pName: String = "",
    var name: String = "",
    @Embedded var block: BlockConfiguration = BlockConfiguration(),
    @Ignore var icon: Drawable? = null,
    @Ignore var selected: Boolean = false
)

@Entity
data class BlockConfiguration(
    var duration: Long = 30600000L,
    var startTimeOfDay: Long = 32400000L,
    var blockNotifications: Boolean = false,
    @TypeConverters(CustomRepetitionPatternConverter::class)
    var days: List<CustomRepetitionPattern> = CustomRepetitionPattern.values().toList()
)
