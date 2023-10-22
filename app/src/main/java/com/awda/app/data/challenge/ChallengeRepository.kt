package com.awda.app.data.challenge

import android.content.Context
import com.awda.app.common.getAppIcon
import com.awda.app.data.challenge.db.ChallengeDao
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.common.models.Resource
import com.awda.app.domain.processors.AppUsageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Created by Abdelrahman Rizq
 */

class ChallengeRepository(
    val context: Context,
    private val dao: ChallengeDao,
    private val appUsageProcessor: AppUsageProcessor,
) {
    suspend fun getInstalledApps() = flow {
        emit(Resource.Success(appUsageProcessor.sortedInstalledApps()))
    }

    suspend fun insertChallenge(challenge: AppChallenge) =
        withContext(Dispatchers.IO) { dao.insert(challenge) }

    suspend fun deleteChallenge(challenge: AppChallenge) =
        withContext(Dispatchers.IO) { dao.delete(challenge) }

    suspend fun updateChallenge(challenge: AppChallenge) =
        withContext(Dispatchers.IO) { dao.update(challenge) }

    suspend fun retrieveChallenges(loadIcons: Boolean = false) = flow {
        fun withIcons(challenges: List<AppChallenge>): MutableList<AppChallenge> {
            val result = mutableListOf<AppChallenge>()
            challenges.forEach { item ->
                val existing = result.firstOrNull { it.createdAt == item.createdAt }
                val icon = existing?.app?.icon ?: context.getAppIcon(item.app.pName)
                val challenge = item.copy(app = item.app.copy(icon = icon))
                result.add(challenge)
            }
            return result
        }

        dao.retrieve().collect { challenges ->
            emit(if (loadIcons) withIcons(challenges) else challenges)
        }
    }
}