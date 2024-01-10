package org.blackcandy.android.models

import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val id: Int,
    val name: String,
    val duration: Double,
    val url: String,
    val albumName: String,
    val artistName: String,
    val format: String,
    val albumImageUrl: ImageURL,
    var isFavorited: Boolean,
) {
    @Serializable
    data class ImageURL(
        val small: String,
        val medium: String,
        val large: String,
    )
}
