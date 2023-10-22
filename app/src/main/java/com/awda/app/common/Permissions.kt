package com.awda.app.common

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Process
import android.provider.Settings
import androidx.core.app.ActivityCompat

/**
 * Created by Abdelrahman Rizq
 */

fun Context.isPermissionGranted(permission: String): Boolean {
    if (permission == Constants.PACKAGE_USAGE_PERMISSION) {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode =
            appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    if (permission == Constants.OVERLAY_PERMISSION) {
        return Settings.canDrawOverlays(this)
    }

    return PackageManager.PERMISSION_GRANTED == checkSelfPermission(permission)
}

fun Activity.requestPermission(permission: String) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
}

fun Context.openPermissionSettings(action: String, highlightPackage: Boolean = true) {
    val intent = if (highlightPackage) {
        Intent(action, Uri.parse("package:$packageName"))
    } else {
        Intent(action)
    }
    startActivity(intent)
}