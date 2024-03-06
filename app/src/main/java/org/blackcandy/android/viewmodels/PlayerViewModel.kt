package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.blackcandy.android.media.MusicServiceController
import org.blackcandy.android.media.MusicState

data class PlayerUiState(
    val musicState: MusicState = MusicState(),
    val currentPosition: Double = 0.0,
)

class PlayerViewModel(
    private val musicServiceController: MusicServiceController,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())

    val uiState =
        combine(
            _uiState,
            musicServiceController.musicState,
            musicServiceController.currentPosition,
        ) { state, musicState, currentPosition ->
            state.copy(
                musicState = musicState,
                currentPosition = currentPosition,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlayerUiState(),
        )

    fun previous() {
        musicServiceController.previous()
    }

    fun next() {
        musicServiceController.next()
    }

    fun play() {
        musicServiceController.play()
    }

    fun pause() {
        musicServiceController.pause()
    }

    fun seekTo(seconds: Double) {
        musicServiceController.seekTo(seconds)
    }
}
