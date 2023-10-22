package com.awda.app.domain.challenge.usecase

import com.awda.app.data.challenge.ChallengeRepository
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

class RetrieveChallengesUseCase(private val repository: ChallengeRepository) :
    AsyncUseCase<Unit, Flow<List<AppChallenge>>> {
    override suspend fun execute(params: Unit): Flow<List<AppChallenge>> {
        return repository.retrieveChallenges(loadIcons = true)
    }
}