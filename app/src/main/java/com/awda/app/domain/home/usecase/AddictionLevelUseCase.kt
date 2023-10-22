package com.awda.app.domain.home.usecase

import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.HomeRepository
import com.awda.app.data.home.models.AddictionLevel
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class AddictionLevelUseCase(private val repository: HomeRepository) :
    AsyncUseCase<Unit, Resource<AddictionLevel>> {
    override suspend fun execute(params: Unit): Resource<AddictionLevel> {
        return repository.getAddictionLevel().first()
    }

}