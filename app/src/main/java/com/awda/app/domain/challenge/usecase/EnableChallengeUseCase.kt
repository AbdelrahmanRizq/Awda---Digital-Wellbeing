package com.awda.app.domain.challenge.usecase

import com.awda.app.data.challenge.ChallengeRepository
import com.awda.app.data.challenge.models.AppChallengeState
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import com.awda.app.domain.common.models.EnableChallenge
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class EnableChallengeUseCase(private val repository: ChallengeRepository) :
    AsyncUseCase<EnableChallenge, Unit> {
    override suspend fun execute(params: EnableChallenge) {
        repository.retrieveChallenges().first()
            .filter { it.createdAt == params.challenge.createdAt }
            .onEach { repository.updateChallenge(it.copy(enabled = params.enabled)) }
            .filter { it.state == AppChallengeState.PENDING }
            .forEach {
                if (!params.enabled) {
                    repository.deleteChallenge(it)
                }
            }

        if (params.enabled && params.challenge.repetitionPattern.default != DefaultRepetitionPattern.ONCE) {
            params.challenge.copy(enabled = true, state = AppChallengeState.PENDING)
                .schedule(repository.context, repository::insertChallenge)
        }
    }
}