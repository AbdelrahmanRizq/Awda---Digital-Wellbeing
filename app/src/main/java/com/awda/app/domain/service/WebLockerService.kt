package com.awda.app.domain.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.content.FileProvider
import com.awda.app.domain.processors.PreferencesProcessor
import org.koin.android.ext.android.inject
import java.io.File


/**
 * Created by Abdelrahman Rizq
 */

class WebLockerService : AccessibilityService() {

    private data class BrowserConfiguration(val pName: String, val addressBarId: String)

    private val preferences: PreferencesProcessor by inject()

    private var uri: Uri? = null
    private val previousUrlDetections = HashMap<String, Long>()
    private val browsers = listOf(
        BrowserConfiguration("com.android.chrome", "com.android.chrome:id/url_bar"),
        BrowserConfiguration("org.mozilla.firefox", "org.mozilla.firefox:id/url_bar_title")
    )

    private companion object {
        private const val NOTIFICATION_TIMEOUT = 300L
        private const val DETECTION_INTERVAL = 2000L
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            packageNames = browsers.map { it.pName }.toTypedArray()
            feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
            notificationTimeout = NOTIFICATION_TIMEOUT
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        serviceInfo = info
        Toast.makeText(this, "Awda Website Blocker Service Is Running", Toast.LENGTH_LONG).show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val node = event.source ?: return
        val browser = browsers.find { it.pName == event.packageName.toString() } ?: return
        val url = node.findAccessibilityNodeInfosByViewId(browser.addressBarId)
            .firstOrNull()?.text ?: return

        check(url.toString(), browser, event.eventTime)
    }

    private fun check(
        url: String,
        browser: BrowserConfiguration,
        eventTime: Long
    ) {
        val id = "${browser.pName}:$url"
        val lastRecordedTime = previousUrlDetections[id] ?: 0

        if (eventTime - lastRecordedTime > DETECTION_INTERVAL) {
            previousUrlDetections[id] = eventTime
            if (preferences.getBlockedWebsites().any { url.contains(it) }) {
                block(browser.pName)
            }
        }
    }

    private fun block(browserPackage: String) {
        Intent(Intent.ACTION_VIEW, uri ?: blockerUri()).apply {
            setPackage(browserPackage)
            putExtra(Browser.EXTRA_APPLICATION_ID, browserPackage)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.let { startActivity(it) }
    }

    private fun blockerUri(): Uri? {
        val path = "awda_web_blocker.html"
        val file = File(filesDir, path)
        assets.open(path).use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        uri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            file
        )

        return uri
    }

    override fun onInterrupt() {}
}

