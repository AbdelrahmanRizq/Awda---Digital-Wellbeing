package com.awda.app.domain.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.awda.app.data.challenge.ChallengeRepository
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.challenge.models.AppChallengeState
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.processors.AppUsageProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

class ChallengeService : Service() {
    private lateinit var handler: Handler
    private val appUsage: AppUsageProcessor by inject()
    private val repository: ChallengeRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        trackEvents()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        job.cancel()
        super.onDestroy()
    }

    private fun trackEvents() {
        val handlerThread = HandlerThread("ChallengesThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        trackRunningChallenges()
        trackPendingChallenges()
    }

    private fun trackRunningChallenges() {
        fun check() {
            scope.launch(Dispatchers.IO) {
                repository.retrieveChallenges().first()
                    .filter { it.state == AppChallengeState.RUNNING }
                    .forEach { challenge ->
                        val usage = appUsage.query(
                            range = TimeRange(
                                challenge.occurrence,
                                challenge.occurrence + challenge.time
                            ),
                            loadIcons = false
                        )
                        val app = usage.find { it.pName == challenge.app.pName }

                        if (app == null) {
                            if (challenge.occurrence + challenge.time < System.currentTimeMillis()) {
                                repository.updateChallenge(challenge.copy(state = AppChallengeState.PASSED))
                            }
                        } else {
                            if (challenge.occurrence + challenge.time > app.timestamp) {
                                repository.updateChallenge(challenge.copy(state = AppChallengeState.LOST))
                            } else {
                                repository.updateChallenge(challenge.copy(state = AppChallengeState.PASSED))
                            }
                        }
                    }
            }
        }

        handler.post(object : Runnable {
            override fun run() {
                check()
                handler.postDelayed(this, 5 * 1000)
            }
        })
    }

    private fun trackPendingChallenges() {
        val alarmAllowance = TimeUnit.MINUTES.toMillis(1)

        suspend fun scheduleNext(challenge: AppChallenge) {
            if (challenge.repetitionPattern.default != DefaultRepetitionPattern.ONCE) {
                val nextChallenge =
                    challenge.copy(state = AppChallengeState.PENDING)
                nextChallenge.schedule(applicationContext, repository::insertChallenge)
            }
        }

        fun check() {
            scope.launch(Dispatchers.IO) {
                repository.retrieveChallenges().first()
                    .filter { it.state == AppChallengeState.PENDING }
                    .forEach { challenge ->
                        if (challenge.occurrence < System.currentTimeMillis() + alarmAllowance) {
                            repository.updateChallenge(challenge.copy(state = AppChallengeState.RUNNING))
                            scheduleNext(challenge)
                        }
                    }
            }
        }

        handler.post(object : Runnable {
            override fun run() {
                check()
                handler.postDelayed(this, alarmAllowance)
            }
        })
    }
}