package org.blackcandy.shared.models

data class AuthenticationResponse(
    val token: String,
    val user: User,
    val cookies: List<String>,
)
