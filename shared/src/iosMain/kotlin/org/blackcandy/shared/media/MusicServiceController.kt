package org.blackcandy.shared.media

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.blackcandy.shared.models.Song

actual class MusicServiceController {
    actual val musicState: StateFlow<MusicState> = MutableStateFlow(MusicState())
    actual val currentPosition: Flow<Double> = MutableStateFlow(0.0)

    actual fun initMediaController(onInitialized: () -> Unit) {
    }

    actual fun updatePlaylist(songs: List<Song>) {
    }

    actual fun play() {
    }

    actual fun pause() {
    }

    actual fun next() {
    }

    actual fun previous() {
    }

    actual fun playOn(index: Int) {
    }

    actual fun seekTo(seconds: Double) {
    }

    actual fun clearPlaylist() {
    }

    actual fun deleteSongFromPlaylist(song: Song) {
    }

    actual fun updateSongInPlaylist(song: Song) {
    }

    actual fun moveSongInPlaylist(
        from: Int,
        to: Int,
    ) {
    }

    actual fun setPlaybackMode(playbackMode: PlaybackMode) {
    }

    actual fun getSongIndex(songId: Int): Int = 0

    actual fun addSongToNext(song: Song): Int = 0

    actual fun addSongToLast(song: Song) {
    }
}
