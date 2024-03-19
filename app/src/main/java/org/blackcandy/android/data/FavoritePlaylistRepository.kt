package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.Song
import org.blackcandy.android.utils.TaskResult

class FavoritePlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun toggleSong(song: Song): TaskResult<Song> {
        val response = if (song.isFavorited) service.removeSongFromFavorite(song.id) else service.addSongToFavorite(song.id)
        return response.asResult()
    }
}
