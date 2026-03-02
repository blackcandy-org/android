package org.blackcandy.shared.media

import platform.MediaPlayer.MPChangePlaybackPositionCommandEvent
import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatusCommandFailed
import platform.MediaPlayer.MPRemoteCommandHandlerStatusSuccess

class MediaRemoteController {
    companion object {
        fun setup(musicServiceController: MusicServiceController) {
            val commandCenter = MPRemoteCommandCenter.sharedCommandCenter()

            commandCenter.playCommand.addTargetWithHandler {
                musicServiceController.play()
                MPRemoteCommandHandlerStatusSuccess
            }

            commandCenter.pauseCommand.addTargetWithHandler {
                musicServiceController.pause()
                MPRemoteCommandHandlerStatusSuccess
            }

            commandCenter.stopCommand.addTargetWithHandler {
                musicServiceController.stop()
                MPRemoteCommandHandlerStatusSuccess
            }

            commandCenter.togglePlayPauseCommand.addTargetWithHandler {
                if (musicServiceController.musicState.value.isPlaying) {
                    musicServiceController.pause()
                } else {
                    musicServiceController.play()
                }
                MPRemoteCommandHandlerStatusSuccess
            }

            commandCenter.nextTrackCommand.addTargetWithHandler {
                musicServiceController.next()
                MPRemoteCommandHandlerStatusSuccess
            }

            commandCenter.previousTrackCommand.addTargetWithHandler {
                musicServiceController.previous()
                MPRemoteCommandHandlerStatusSuccess
            }

            commandCenter.changePlaybackPositionCommand.addTargetWithHandler { event ->
                val positionEvent =
                    event as? MPChangePlaybackPositionCommandEvent
                        ?: return@addTargetWithHandler MPRemoteCommandHandlerStatusCommandFailed

                musicServiceController.seekTo(positionEvent.positionTime)
                MPRemoteCommandHandlerStatusSuccess
            }
        }
    }
}
