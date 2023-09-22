package org.blackcandy.android.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val isAdmin: Boolean,
)
