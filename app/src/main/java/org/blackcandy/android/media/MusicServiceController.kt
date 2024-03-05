package org.blackcandy.android.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.blackcandy.android.models.Song

class MusicServiceController(
    private val appContext: Context,
) {
    private var controller: MediaController? = null
    private val _musicState = MutableStateFlow(MusicState())

    val musicState = _musicState.asStateFlow()

    fun initMediaController(onInitialized: () -> Unit) {
        val controllerFuture =
            MediaController.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, MusicService::class.java)),
            ).buildAsync()

        controllerFuture.addListener({
            controller = controllerFuture.get()
            controller?.addListener(
                object : Player.Listener {
                    override fun onEvents(
                        player: Player,
                        events: Player.Events,
                    ) {
                        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                            _musicState.update { it.copy(playbackState = player.playbackState) }
                        }

                        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                            val currentMediaId = player.currentMediaItem?.mediaId
                            val currentSong = _musicState.value.playlist.find { it.id.toString() == currentMediaId }

                            _musicState.update { it.copy(currentSong = currentSong) }
                        }

                        if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
                            _musicState.update { it.copy(isPlaying = player.isPlaying) }
                        }
                    }
                },
            )
            onInitialized()
        }, MoreExecutors.directExecutor())
    }

    fun updatePlaylist(songs: List<Song>) {
        val mediaItems = songs.map { it.toMediaItem() }

        controller?.setMediaItems(mediaItems)
        _musicState.update { it.copy(playlist = songs) }
    }

    fun play() {
        controller?.play()
    }

    fun pause() {
        controller?.pause()
    }

    fun next() {
        controller?.seekToNext()
    }
}
