package org.blackcandy.shared.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.blackcandy.shared.data.CurrentPlaylistRepository
import org.blackcandy.shared.media.MusicServiceController
import org.blackcandy.shared.utils.TaskResult

class MusicServiceViewModel(
    private val currentPlaylistRepository: CurrentPlaylistRepository,
    private val musicServiceController: MusicServiceController,
) : ViewModel() {
    fun setupMusicServiceController() {
        musicServiceController.initMediaController {
            getCurrentPlaylist()
        }
    }

    fun getCurrentPlaylist() {
        viewModelScope.launch {
            when (val result = currentPlaylistRepository.getSongs()) {
                is TaskResult.Success -> musicServiceController.updateSongs(result.data)
                is TaskResult.Failure -> Unit
            }
        }
    }
}
