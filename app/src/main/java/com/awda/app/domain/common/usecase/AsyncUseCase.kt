package com.awda.app.domain.common.usecase

/**
 * Created by Abdelrahman Rizq
 */

interface AsyncUseCase<Params, Result> {
    suspend fun execute(params: Params): Result
}