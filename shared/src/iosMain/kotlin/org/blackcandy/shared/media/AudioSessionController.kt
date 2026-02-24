package org.blackcandy.shared.media

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive

class AudioSessionController {
    companion object {
        @OptIn(ExperimentalForeignApi::class)
        fun setup(musicServiceController: MusicServiceController) {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, error = null)
            audioSession.setActive(true, null)
        }
    }
}
