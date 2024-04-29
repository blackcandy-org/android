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

    suspend fun replaceWithAlbumSongs(albumId: Int): TaskResult<List<Song>> {
        return service.replaceCurrentPlaylistWithAlbumSongs(albumId).asResult()
    }

    suspend fun replaceWithPlaylistSongs(playlistId: Int): TaskResult<List<Song>> {
        return service.replaceCurrentPlaylistWithPlaylistSongs(playlistId).asResult()
    }

    suspend fun addSongToNext(
        songId: Int,
        currentSongId: Int,
    ): TaskResult<Song> {
        return service.addSongToCurrentPlaylist(songId, currentSongId, null).asResult()
    }

    suspend fun addSongToLast(songId: Int): TaskResult<Song> {
        return service.addSongToCurrentPlaylist(songId, null, "last").asResult()
    }
}
