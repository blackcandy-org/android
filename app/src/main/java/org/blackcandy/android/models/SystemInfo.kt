package org.blackcandy.android.models

import kotlinx.serialization.Serializable

@Serializable
data class SystemInfo(
    val version: Version,
    var serverAddress: String? = null,
) {
    companion object {
        const val SUPPORTED_MINIMUM_MAJOR_VERSION = 3
    }

    val isSupported get() = version.major >= SUPPORTED_MINIMUM_MAJOR_VERSION

    @Serializable
    data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int,
        val pre: String,
    )
}
