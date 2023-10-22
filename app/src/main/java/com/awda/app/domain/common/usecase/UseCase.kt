package com.awda.app.domain.common.usecase

/**
 * Created by Abdelrahman Rizq
 */

interface UseCase<Params, Result> {
    fun execute(params: Params): Result
}