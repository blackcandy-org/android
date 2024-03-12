package org.blackcandy.android.utils

sealed interface TaskResult<out T> {
    data class Success<T>(val data: T) : TaskResult<T>

    data class Failure(val message: String?) : TaskResult<Nothing>
}
