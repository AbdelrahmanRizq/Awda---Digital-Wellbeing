package com.awda.app.domain.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.presentation.biometric.BiometricAuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

class BiometricAuthService : AccessibilityService() {
    private val repository: AppSettingsRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val interval = TimeUnit.MINUTES.toMillis(20)

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        scope.launch(Dispatchers.IO) {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                val app = repository.getSecureApps().first()
                    .find { it.key == event.packageName?.toString() }

                if (app != null && System.currentTimeMillis() - app.lastAuthentication > interval) {
                    BiometricAuthActivity.callbacks =
                        object : BiometricAuthActivity.Callbacks {
                            override fun onAuthenticationSucceeded() {
                                scope.launch(Dispatchers.IO) {
                                    app.lastAuthentication = System.currentTimeMillis()
                                    repository.setSecureApp(app)
                                }
                            }
                        }
                    withContext(Dispatchers.Main) {
                        BiometricAuthActivity.start(
                            this@BiometricAuthService,
                            app.key
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onInterrupt() {}
}
