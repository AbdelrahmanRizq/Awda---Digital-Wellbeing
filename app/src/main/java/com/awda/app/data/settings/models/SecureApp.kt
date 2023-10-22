package com.awda.app.data.settings.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.awda.app.data.challenge.models.InstalledApp

/**
 * Created by Abdelrahman Rizq
 */
@Entity(tableName = "secure_app")
data class SecureApp(
    @PrimaryKey var key: String = "",
    @Embedded var info: InstalledApp = InstalledApp(),
    var lastAuthentication: Long = 0L
)