package org.blackcandy.android.models

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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

    fun toMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setMediaId(id.toString())
            .setUri(url)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(name)
                    .setArtist(artistName)
                    .setAlbumTitle(albumName)
                    .setArtworkUri(Uri.parse(albumImageUrl.large))
                    .build(),
            )
            .build()
    }
}
