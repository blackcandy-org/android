package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import org.blackcandy.android.media.MusicServiceController

class MiniPlayerViewModel(
    private val musicServiceController: MusicServiceController,
) : ViewModel() {
    fun play() {
        musicServiceController.play()
    }
}
