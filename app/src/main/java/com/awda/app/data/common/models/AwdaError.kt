package com.awda.app.data.common.models

import com.awda.app.data.common.models.ErrorTypes.PACKAGE_USAGE_PERMISSION_NOT_GRANTED
import com.awda.app.data.common.models.ErrorTypes.REQUEST_SCHEDULE_PERMISSION_NOT_GRANTED

/**
 * Created by Abdelrahman Rizq
 */

data class AwdaError(
    val type: String,
    val message: String,
    val permission: String? = null,
    val cause: Exception? = null
) {
    val header = when (type) {
        PACKAGE_USAGE_PERMISSION_NOT_GRANTED -> {
            "Package Usage Permission Not Granted"
        }

        REQUEST_SCHEDULE_PERMISSION_NOT_GRANTED -> {
            "Schedule Alarm Permission Not Granted"
        }

        else -> {
            "Unknown Error"
        }
    }
}

object ErrorTypes {
    const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
    const val PACKAGE_USAGE_PERMISSION_NOT_GRANTED = "PACKAGE_USAGE_PERMISSION_NOT_GRANTED"
    const val REQUEST_SCHEDULE_PERMISSION_NOT_GRANTED = "REQUEST_SCHEDULE_PERMISSION_NOT_GRANTED"
}