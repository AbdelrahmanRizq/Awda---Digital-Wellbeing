package com.awda.app.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.awda.app.domain.service.AppLockerService
import com.awda.app.domain.service.ChallengeService

/**
 * Created by Abdelrahman Rizq
 */

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = goAsync {
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            startServices(context)
        }
    }

    private fun startServices(context: Context) {
        val lockerIntent = Intent(context, AppLockerService::class.java)
        context.startService(lockerIntent)

        val challengeIntent = Intent(context, ChallengeService::class.java)
        context.startService(challengeIntent)
    }
}