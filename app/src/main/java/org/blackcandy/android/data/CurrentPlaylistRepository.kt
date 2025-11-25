package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.Song
import org.blackcandy.shared.utils.TaskResult

class CurrentPlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSongs(): TaskResult<List<Song>> = service.getSongsFromCurrentPlaylist().asResult()

    suspend fun removeAllSongs(): TaskResult<Unit> = service.removeAllSongsFromCurrentPlaylist().asResult()

    suspend fun removeSong(songId: Int): TaskResult<Unit> = service.removeSongFromCurrentPlaylist(songId).asResult()

    suspend fun moveSong(
        songId: Int,
        destinationSongId: Int,
    ): TaskResult<Unit> = service.moveSongInCurrentPlaylist(songId, destinationSongId).asResult()

    suspend fun replaceWithAlbumSongs(albumId: Int): TaskResult<List<Song>> =
        service.replaceCurrentPlaylistWithAlbumSongs(albumId).asResult()

    suspend fun replaceWithPlaylistSongs(playlistId: Int): TaskResult<List<Song>> =
        service.replaceCurrentPlaylistWithPlaylistSongs(playlistId).asResult()

    suspend fun addSongToNext(
        songId: Int,
        currentSongId: Int,
    ): TaskResult<Song> = service.addSongToCurrentPlaylist(songId, currentSongId, null).asResult()

    suspend fun addSongToLast(songId: Int): TaskResult<Song> = service.addSongToCurrentPlaylist(songId, null, "last").asResult()
}
