package org.blackcandy.shared.media

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.blackcandy.shared.models.Song

actual class MusicServiceController {
    actual val musicState: StateFlow<MusicState>
        get() = TODO("Not yet implemented")
    actual val currentPosition: Flow<Double>
        get() = TODO("Not yet implemented")

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

    actual fun moveSongInPlaylist(from: Int, to: Int) {
    }

    actual fun setPlaybackMode(playbackMode: PlaybackMode) {
    }

    actual fun getSongIndex(songId: Int): Int {
        TODO("Not yet implemented")
    }

    actual fun addSongToNext(song: Song): Int {
        TODO("Not yet implemented")
    }

    actual fun addSongToLast(song: Song) {
    }

}