package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import org.blackcandy.android.media.MusicServiceController

class MiniPlayerViewModel(
    private val musicServiceController: MusicServiceController,
) : ViewModel() {
    val musicState = musicServiceController.musicState

    fun play() {
        musicServiceController.play()
    }

    fun pause() {
        musicServiceController.pause()
    }

    fun next() {
        musicServiceController.next()
    }
}
