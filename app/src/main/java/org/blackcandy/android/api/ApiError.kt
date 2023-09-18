package org.blackcandy.android.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val type: String,
    val message: String,
)
