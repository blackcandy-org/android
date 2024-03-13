package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.blackcandy.android.data.FavoritePlaylistRepository
import org.blackcandy.android.media.MusicServiceController
import org.blackcandy.android.models.AlertMessage
import org.blackcandy.android.models.MusicState
import org.blackcandy.android.utils.TaskResult

data class PlayerUiState(
    val musicState: MusicState = MusicState(),
    val currentPosition: Double = 0.0,
    val alertMessage: AlertMessage? = null,
    val isPlaylistsVisible: Boolean = false,
)

class PlayerViewModel(
    private val musicServiceController: MusicServiceController,
    private val favoritePlaylistRepository: FavoritePlaylistRepository,
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

    fun nextMode() {
        musicServiceController.setPlaybackMode(uiState.value.musicState.playbackMode.next)
    }

    fun toggleFavorite() {
        val currentSong = uiState.value.musicState.currentSong ?: return

        viewModelScope.launch {
            when (val result = favoritePlaylistRepository.toggleSong(currentSong)) {
                is TaskResult.Success -> {
                    val toggledSong = result.data
                    val updatedPlaylist =
                        uiState.value.musicState.playlist.map { song ->
                            if (song.id == toggledSong.id) toggledSong else song
                        }

                    musicServiceController.updatePlaylist(updatedPlaylist)
                }
                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun setPlaylistVisibility(isVisible: Boolean) {
        _uiState.update { it.copy(isPlaylistsVisible = isVisible) }
    }

    fun alertMessageShown() {
        _uiState.update { it.copy(alertMessage = null) }
    }
}
