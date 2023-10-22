package com.awda.app.domain.intro.usecase

import com.awda.app.data.intro.AppIntroRepository
import com.awda.app.domain.common.usecase.UseCase

/**
 * Created by Abdelrahman Rizq
 */

class SetAppIntroCompletedUseCase(private val repository: AppIntroRepository) :
    UseCase<Unit, Unit> {
    override fun execute(params: Unit) {
        return repository.setAppIntroCompleted()
    }
}