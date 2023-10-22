package com.awda.app.common

import android.content.Context
import android.provider.Settings
import androidx.appcompat.content.res.AppCompatResources
import com.awda.app.R

/**
 * Created by Abdelrahman Rizq
 */

fun Context.getAppIcon(packageName: String) = try {
    packageManager.getPackageInfo(packageName, 0).applicationInfo.loadIcon(packageManager)
} catch (e: Exception) {
    AppCompatResources.getDrawable(this, R.drawable.ic_unavailable)
}


fun Context.getAppName(packageName: String) = try {
    packageManager.getPackageInfo(packageName, 0).applicationInfo.loadLabel(packageManager)
        .toString()
} catch (e: Exception) {
    "Unknown App"
}


fun Context.isAccessibilityServiceRunning(name: String) =
    Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        .split(":".toRegex()).toTypedArray().any { it.contains(name) }
