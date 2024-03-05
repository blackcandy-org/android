package org.blackcandy.android.media

import androidx.media3.common.Player
import org.blackcandy.android.models.Song

data class MusicState(
    val playlist: List<Song> = emptyList(),
    val playbackState: Int = Player.STATE_IDLE,
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
) {
    val hasCurrentSong: Boolean get() = currentSong != null
}
