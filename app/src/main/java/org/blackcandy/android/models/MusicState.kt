package org.blackcandy.android.models

import androidx.media3.common.Player

data class MusicState(
    val playlist: List<Song> = emptyList(),
    val playbackState: Int = Player.STATE_IDLE,
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.NO_REPEAT,
) {
    val hasCurrentSong: Boolean get() = currentSong != null
    val isLoading: Boolean get() = playbackState == Player.STATE_BUFFERING
}
