package com.awda.app.domain.intro.usecase

import com.awda.app.data.intro.AppIntroRepository
import com.awda.app.domain.common.usecase.UseCase

/**
 * Created by Abdelrahman Rizq
 */

class GetAppIntroCompletedUseCase(private val repository: AppIntroRepository) :
    UseCase<Unit, Boolean> {
    override fun execute(params: Unit): Boolean {
        return repository.getAppIntroCompleted()
    }

}