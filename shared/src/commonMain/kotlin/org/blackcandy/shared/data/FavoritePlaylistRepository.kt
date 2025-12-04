package org.blackcandy.shared.data

import org.blackcandy.shared.api.BlackCandyService
import org.blackcandy.shared.models.Song
import org.blackcandy.shared.utils.TaskResult

class FavoritePlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun toggleSong(song: Song): TaskResult<Song> {
        val response = if (song.isFavorited) service.removeSongFromFavorite(song.id) else service.addSongToFavorite(song.id)
        return response.asResult()
    }
}
