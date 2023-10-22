package com.awda.app.data.common.models

/**
 * Created by Abdelrahman Rizq
 */

sealed class Resource<T>(val data: T? = null, val error: AwdaError? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: AwdaError) : Resource<T>(error = error)
}