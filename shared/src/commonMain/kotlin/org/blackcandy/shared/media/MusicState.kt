package org.blackcandy.shared.media

import org.blackcandy.shared.models.Song

data class MusicState(
    val playlist: List<Song> = emptyList(),
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val currentSong: Song? = null,
    val playbackMode: PlaybackMode = PlaybackMode.NO_REPEAT,
) {
    val hasCurrentSong: Boolean get() = currentSong != null
    val isLoading: Boolean get() = playbackState == PlaybackState.BUFFERING
    val isPlaying: Boolean get() = playbackState == PlaybackState.PLAYING
}
