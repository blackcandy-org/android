package org.blackcandy.shared.data

import org.blackcandy.shared.api.BlackCandyService
import org.blackcandy.shared.models.Song
import org.blackcandy.shared.utils.TaskResult

class CurrentPlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSongs(): TaskResult<List<Song>> = service.getSongsFromCurrentPlaylist().asResult()

    suspend fun removeAllSongs(): TaskResult<Unit> = service.removeAllSongsFromCurrentPlaylist().asResult()

    suspend fun removeSong(songId: Long): TaskResult<Unit> = service.removeSongFromCurrentPlaylist(songId).asResult()

    suspend fun moveSong(
        songId: Long,
        destinationSongId: Long,
    ): TaskResult<Unit> = service.moveSongInCurrentPlaylist(songId, destinationSongId).asResult()

    suspend fun replaceWithAlbumSongs(albumId: Long): TaskResult<List<Song>> =
        service.replaceCurrentPlaylistWithAlbumSongs(albumId).asResult()

    suspend fun replaceWithPlaylistSongs(playlistId: Long): TaskResult<List<Song>> =
        service.replaceCurrentPlaylistWithPlaylistSongs(playlistId).asResult()

    suspend fun addSongToNext(
        songId: Long,
        currentSongId: Long,
    ): TaskResult<Song> = service.addSongToCurrentPlaylist(songId, currentSongId, null).asResult()

    suspend fun addSongToLast(songId: Long): TaskResult<Song> = service.addSongToCurrentPlaylist(songId, null, "last").asResult()
}
