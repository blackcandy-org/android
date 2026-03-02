package org.blackcandy.shared.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.blackcandy.shared.data.CurrentPlaylistRepository
import org.blackcandy.shared.data.UserRepository
import org.blackcandy.shared.media.MusicServiceController
import org.blackcandy.shared.models.AlertMessage
import org.blackcandy.shared.models.Song
import org.blackcandy.shared.utils.TaskResult
import org.blackcandy.shared.utils.Theme
import org.blackcandy.shared.utils.updateAppTheme

data class WebUiState(
    val alertMessage: AlertMessage? = null,
)

class WebViewModel(
    private val userRepository: UserRepository,
    private val currentPlaylistRepository: CurrentPlaylistRepository,
    private val musicServiceController: MusicServiceController,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WebUiState())

    val uiState = _uiState.asStateFlow()

    fun logout(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            userRepository.logout()
            onSuccess()
        }
    }

    fun showFlashMessage(message: String) {
        _uiState.update { it.copy(alertMessage = AlertMessage.String(message)) }
    }

    fun alertMessageShown() {
        _uiState.update { it.copy(alertMessage = null) }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            updateAppTheme(theme)
        }
    }

    fun playAlbum(albumId: Long) {
        viewModelScope.launch {
            when (val result = currentPlaylistRepository.replaceWithAlbumSongs(albumId)) {
                is TaskResult.Success -> {
                    playSongs(result.data)
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun playAlbumBeginWith(
        albumId: Long,
        songId: Long,
    ) {
        viewModelScope.launch {
            when (val result = currentPlaylistRepository.replaceWithAlbumSongs(albumId)) {
                is TaskResult.Success -> {
                    playSongsBeginWith(result.data, songId)
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun playPlaylist(playlistId: Long) {
        viewModelScope.launch {
            when (val result = currentPlaylistRepository.replaceWithPlaylistSongs(playlistId)) {
                is TaskResult.Success -> {
                    playSongs(result.data)
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun playPlaylistBeginWith(
        playlistId: Long,
        songId: Long,
    ) {
        viewModelScope.launch {
            when (val result = currentPlaylistRepository.replaceWithPlaylistSongs(playlistId)) {
                is TaskResult.Success -> {
                    playSongsBeginWith(result.data, songId)
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun playNow(songId: Long) {
        viewModelScope.launch {
            val index = musicServiceController.getSongIndex(songId)

            if (index != -1) {
                musicServiceController.playOn(index)
            } else {
                val currentSong = musicServiceController.musicState.value.currentSong ?: return@launch

                when (
                    val result =
                        currentPlaylistRepository.addSongToNext(songId, currentSong.id)
                ) {
                    is TaskResult.Success -> {
                        val songIndex = musicServiceController.addSongToNext(result.data)
                        musicServiceController.playOn(songIndex)

                        _uiState.update {
                            it.copy(
                                alertMessage = AlertMessage.LocalizedString(AlertMessage.DefinedMessages.ADDED_TO_PLAYLIST),
                            )
                        }
                    }

                    is TaskResult.Failure -> {
                        _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                    }
                }
            }
        }
    }

    fun playNext(songId: Long) {
        viewModelScope.launch {
            val currentSong = musicServiceController.musicState.value.currentSong ?: return@launch

            when (val result = currentPlaylistRepository.addSongToNext(songId, currentSong.id)) {
                is TaskResult.Success -> {
                    musicServiceController.addSongToNext(result.data)
                    _uiState.update { it.copy(alertMessage = AlertMessage.LocalizedString(AlertMessage.DefinedMessages.ADDED_TO_PLAYLIST)) }
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun playLast(songId: Long) {
        viewModelScope.launch {
            when (val result = currentPlaylistRepository.addSongToLast(songId)) {
                is TaskResult.Success -> {
                    musicServiceController.addSongToLast(result.data)
                    _uiState.update { it.copy(alertMessage = AlertMessage.LocalizedString(AlertMessage.DefinedMessages.ADDED_TO_PLAYLIST)) }
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    private fun playSongs(songs: List<Song>) {
        musicServiceController.updateSongs(songs)
        musicServiceController.playOn(0)
    }

    private fun playSongsBeginWith(
        songs: List<Song>,
        songId: Long,
    ) {
        musicServiceController.updateSongs(songs)

        val index = musicServiceController.getSongIndex(songId)

        if (index != -1) {
            musicServiceController.playOn(index)
        } else {
            musicServiceController.playOn(0)
        }
    }
}
