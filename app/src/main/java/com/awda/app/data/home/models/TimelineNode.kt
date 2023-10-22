package com.awda.app.data.home.models

/**
 * Created by Abdelrahman Rizq
 */

data class TimelineNode(
    val appUsage: CombinedAppUsage,
    val usageTime: Long,
    val timestamp: Long,
    val start: Long
)