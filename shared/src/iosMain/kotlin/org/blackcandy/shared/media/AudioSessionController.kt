package org.blackcandy.shared.media

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionInterruptionNotification
import platform.AVFAudio.AVAudioSessionInterruptionOptionKey
import platform.AVFAudio.AVAudioSessionInterruptionOptionShouldResume
import platform.AVFAudio.AVAudioSessionInterruptionTypeBegan
import platform.AVFAudio.AVAudioSessionInterruptionTypeEnded
import platform.AVFAudio.AVAudioSessionInterruptionTypeKey
import platform.AVFAudio.AVAudioSessionPortDescription
import platform.AVFAudio.AVAudioSessionPortHeadphones
import platform.AVFAudio.AVAudioSessionRouteChangeNotification
import platform.AVFAudio.AVAudioSessionRouteChangePreviousRouteKey
import platform.AVFAudio.AVAudioSessionRouteChangeReasonKey
import platform.AVFAudio.AVAudioSessionRouteChangeReasonOldDeviceUnavailable
import platform.AVFAudio.AVAudioSessionRouteDescription
import platform.AVFAudio.setActive
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNumber
import platform.Foundation.NSOperationQueue
import platform.darwin.NSObjectProtocol

class AudioSessionController {
    companion object {
        private var interruptionObserver: NSObjectProtocol? = null
        private var routeChangeObserver: NSObjectProtocol? = null

        @OptIn(ExperimentalForeignApi::class)
        fun setup(musicServiceController: MusicServiceController) {
            val audioSession = AVAudioSession.sharedInstance()
            val notificationCenter = NSNotificationCenter.defaultCenter

            audioSession.setCategory(AVAudioSessionCategoryPlayback, error = null)
            audioSession.setActive(true, null)

            interruptionObserver =
                notificationCenter.addObserverForName(
                    name = AVAudioSessionInterruptionNotification,
                    `object` = audioSession,
                    queue = NSOperationQueue.mainQueue,
                    usingBlock = { notification ->
                        handleInterruption(notification, musicServiceController)
                    },
                )

            routeChangeObserver =
                notificationCenter.addObserverForName(
                    name = AVAudioSessionRouteChangeNotification,
                    `object` = null,
                    queue = NSOperationQueue.mainQueue,
                    usingBlock = { notification ->
                        handleRouteChange(notification, musicServiceController)
                    },
                )
        }

        private fun handleInterruption(
            notification: NSNotification?,
            musicServiceController: MusicServiceController,
        ) {
            val userInfo = notification?.userInfo ?: return
            val typeValue = (userInfo[AVAudioSessionInterruptionTypeKey] as? NSNumber)?.unsignedIntegerValue ?: return

            when (typeValue) {
                AVAudioSessionInterruptionTypeBegan -> {
                    musicServiceController.pause()
                }

                AVAudioSessionInterruptionTypeEnded -> {
                    val optionsValue = (userInfo[AVAudioSessionInterruptionOptionKey] as? NSNumber)?.unsignedIntegerValue ?: return

                    if (optionsValue and AVAudioSessionInterruptionOptionShouldResume != 0UL) {
                        musicServiceController.play()
                    }
                }

                else -> {
                    musicServiceController.pause()
                }
            }
        }

        private fun handleRouteChange(
            notification: NSNotification?,
            musicServiceController: MusicServiceController,
        ) {
            val userInfo = notification?.userInfo ?: return
            val reasonValue = (userInfo[AVAudioSessionRouteChangeReasonKey] as? NSNumber)?.unsignedIntegerValue ?: return

            when (reasonValue) {
                AVAudioSessionRouteChangeReasonOldDeviceUnavailable -> {
                    val previousRoute = userInfo[AVAudioSessionRouteChangePreviousRouteKey] as? AVAudioSessionRouteDescription ?: return

                    if (hasHeadphones(previousRoute)) {
                        musicServiceController.pause()
                    }
                }
            }
        }

        private fun hasHeadphones(routeDescription: AVAudioSessionRouteDescription): Boolean =
            routeDescription.outputs.any { output ->
                (output as? AVAudioSessionPortDescription)?.portType == AVAudioSessionPortHeadphones
            }
    }
}
