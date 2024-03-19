package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.Song
import org.blackcandy.android.utils.TaskResult

class CurrentPlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSongs(): TaskResult<List<Song>> {
        return service.getSongsFromCurrentPlaylist().asResult()
    }

    suspend fun removeAllSongs(): TaskResult<Unit> {
        return service.removeAllSongsFromCurrentPlaylist().asResult()
    }

    suspend fun removeSong(songId: Int): TaskResult<Unit> {
        return service.removeSongFromCurrentPlaylist(songId).asResult()
    }

    suspend fun moveSong(
        songId: Int,
        destinationSongId: Int,
    ): TaskResult<Unit> {
        return service.moveSongInCurrentPlaylist(songId, destinationSongId).asResult()
    }
}
