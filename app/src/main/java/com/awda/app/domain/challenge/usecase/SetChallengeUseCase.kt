package com.awda.app.domain.challenge.usecase

import com.awda.app.data.challenge.ChallengeRepository
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.common.models.AwdaError
import com.awda.app.domain.common.usecase.AsyncUseCase

/**
 * Created by Abdelrahman Rizq
 */

class SetChallengeUseCase(private val repository: ChallengeRepository) :
    AsyncUseCase<AppChallenge, AwdaError?> {
    override suspend fun execute(params: AppChallenge): AwdaError? {
        return params.schedule(repository.context, repository::insertChallenge)
    }
}