package com.awda.app.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.awda.app.common.Constants
import com.awda.app.data.challenge.ChallengeRepository
import com.awda.app.data.challenge.models.AppChallengeState
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by Abdelrahman Rizq
 */

class ChallengeAlarmReceiver : BroadcastReceiver() {
    private val repository: ChallengeRepository by inject(ChallengeRepository::class.java)
    override fun onReceive(context: Context?, intent: Intent) = goAsync {
        val challenge =
            repository.retrieveChallenges().first()
                .firstOrNull {
                    it.occurrence == intent.getLongExtra(
                        Constants.CHALLENGE_OCCURRENCE,
                        0L
                    ) && it.state == AppChallengeState.PENDING
                } ?: return@goAsync

        if (challenge.occurrence <= System.currentTimeMillis()) {
            repository.updateChallenge(challenge.copy(state = AppChallengeState.RUNNING))
            if (challenge.repetitionPattern.default != DefaultRepetitionPattern.ONCE) {
                delay(1000)
                val nextChallenge = challenge.copy(state = AppChallengeState.PENDING)
                nextChallenge.schedule(context!!, repository::insertChallenge)
            }
        }
    }
}