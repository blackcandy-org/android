package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.blackcandy.android.data.CurrentPlaylistRepository
import org.blackcandy.android.data.UserRepository
import org.blackcandy.android.media.MusicServiceController

class MainViewModel(
    userRepository: UserRepository,
    private val currentPlaylistRepository: CurrentPlaylistRepository,
    private val musicServiceController: MusicServiceController,
) : ViewModel() {
    val currentUserFlow = userRepository.getCurrentUserFlow()

    fun setupMusicServiceController() {
        musicServiceController.initMediaController {
            getCurrentPlaylist()
        }
    }

    fun getCurrentPlaylist() {
        viewModelScope.launch {
            musicServiceController.updatePlaylist(currentPlaylistRepository.getSongs())
        }
    }
}
