package org.blackcandy.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val email: String,
    val isAdmin: Boolean,
)
