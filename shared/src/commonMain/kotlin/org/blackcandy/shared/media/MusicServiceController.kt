package org.blackcandy.shared.media

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.blackcandy.shared.models.Song

expect class MusicServiceController {
    val musicState: StateFlow<MusicState>
    val currentPosition: Flow<Double>

    fun initMediaController(onInitialized: () -> Unit)

    fun updateSongs(songs: List<Song>)

    fun play()

    fun pause()

    fun next()

    fun previous()

    fun playOn(index: Int)

    fun seekTo(seconds: Double)

    fun clearPlaylist()

    fun deleteSongFromPlaylist(song: Song)

    fun updateSongInPlaylist(song: Song)

    fun moveSongInPlaylist(
        from: Int,
        to: Int,
    )

    fun setPlaybackMode(playbackMode: PlaybackMode)

    fun getSongIndex(songId: Int): Int

    fun addSongToNext(song: Song): Int

    fun addSongToLast(song: Song)
}
