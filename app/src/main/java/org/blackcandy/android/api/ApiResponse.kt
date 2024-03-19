package org.blackcandy.android.api

import org.blackcandy.android.utils.TaskResult

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>

    data class Failure(val exception: ApiException) : ApiResponse<Nothing>

    fun orNull(): T? =
        when (this) {
            is Success -> data
            is Failure -> null
        }

    fun orThrow(): T =
        when (this) {
            is Success -> data
            is Failure -> throw exception
        }

    fun asResult(): TaskResult<T> =
        when (this) {
            is Success -> TaskResult.Success(data)
            is Failure -> TaskResult.Failure(exception.message)
        }
}
