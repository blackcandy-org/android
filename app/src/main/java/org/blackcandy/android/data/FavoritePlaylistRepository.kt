package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.Song

class FavoritePlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun addSong(songId: Int): Song {
        return service.addSongToFavorite(songId)
    }

    suspend fun deleteSong(songId: Int): Song {
        return service.deleteSongFromFavorite(songId)
    }
}
