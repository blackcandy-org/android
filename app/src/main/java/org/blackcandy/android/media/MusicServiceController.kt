package org.blackcandy.android.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import org.blackcandy.android.models.MusicState
import org.blackcandy.android.models.PlaybackMode
import org.blackcandy.android.models.Song
import kotlin.time.Duration.Companion.milliseconds

class MusicServiceController(
    private val appContext: Context,
) {
    private var controller: MediaController? = null
    private val _musicState = MutableStateFlow(MusicState())

    val musicState = _musicState.asStateFlow()
    val currentPosition =
        flow {
            while (currentCoroutineContext().isActive) {
                val currentPosition = (controller?.currentPosition ?: 0) / 1000.0
                emit(currentPosition)
                delay(1.milliseconds)
            }
        }

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

                            if (player.playbackState == Player.STATE_ENDED) {
                                player.seekToDefaultPosition(0)
                                player.stop()
                            }
                        }

                        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                            updateCurrentSong()
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

        DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {
                override fun getOldListSize() = controller?.mediaItemCount ?: 0

                override fun getNewListSize() = mediaItems.size

                override fun areItemsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int,
                ) = controller?.getMediaItemAt(oldItemPosition)?.mediaId == mediaItems[newItemPosition].mediaId

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int,
                ) = controller?.getMediaItemAt(oldItemPosition) == mediaItems[newItemPosition]
            },
        ).dispatchUpdatesTo(
            object : ListUpdateCallback {
                override fun onInserted(
                    position: Int,
                    count: Int,
                ) {
                    controller?.addMediaItems(position, mediaItems.subList(position, position + count))
                }

                override fun onRemoved(
                    position: Int,
                    count: Int,
                ) {
                    controller?.removeMediaItems(position, position + count)
                }

                override fun onMoved(
                    fromPosition: Int,
                    toPosition: Int,
                ) {
                    controller?.moveMediaItem(fromPosition, toPosition)
                }

                override fun onChanged(
                    position: Int,
                    count: Int,
                    payload: Any?,
                ) {
                    controller?.replaceMediaItems(position, position + count, mediaItems.subList(position, position + count))
                }
            },
        )

        _musicState.update { it.copy(playlist = songs) }
    }

    fun play() {
        controller?.play()
    }

    fun pause() {
        controller?.pause()
    }

    fun next() {
        controller?.run {
            seekToNext()
            play()
        }
    }

    fun previous() {
        controller?.run {
            seekToPrevious()
            play()
        }
    }

    fun playOn(index: Int) {
        controller?.run {
            seekToDefaultPosition(index)
            play()
        }
    }

    fun seekTo(seconds: Double) {
        controller?.seekTo((seconds * 1000).toLong())
    }

    fun clearPlaylist() {
        updatePlaylist(emptyList())
    }

    fun deleteSongFromPlaylist(song: Song) {
        val songs = musicState.value.playlist.toMutableList().apply { remove(song) }
        updatePlaylist(songs)
    }

    fun updateSongInPlaylist(song: Song) {
        val songs = musicState.value.playlist.map { if (it.id == song.id) song else it }
        updatePlaylist(songs)

        if (song.id.toString() == controller?.currentMediaItem?.mediaId) {
            updateCurrentSong()
        }
    }

    fun moveSongInPlaylist(
        from: Int,
        to: Int,
    ) {
        val songs = musicState.value.playlist.toMutableList().apply { add(to, removeAt(from)) }
        updatePlaylist(songs)
    }

    fun setPlaybackMode(playbackMode: PlaybackMode) {
        when (playbackMode) {
            PlaybackMode.NO_REPEAT -> {
                controller?.run {
                    setRepeatMode(Player.REPEAT_MODE_OFF)
                    setShuffleModeEnabled(false)
                }
            }

            PlaybackMode.REPEAT -> {
                controller?.run {
                    setRepeatMode(Player.REPEAT_MODE_ALL)
                    setShuffleModeEnabled(false)
                }
            }

            PlaybackMode.REPEAT_ONE -> {
                controller?.run {
                    setRepeatMode(Player.REPEAT_MODE_ONE)
                    setShuffleModeEnabled(false)
                }
            }

            PlaybackMode.SHUFFLE -> {
                controller?.run {
                    setRepeatMode(Player.REPEAT_MODE_OFF)
                    setShuffleModeEnabled(true)
                }
            }
        }

        _musicState.update { it.copy(playbackMode = playbackMode) }
    }

    fun getSongIndex(songId: Int): Int {
        return musicState.value.playlist.indexOfFirst { it.id == songId }
    }

    fun addSongToNext(song: Song): Int {
        val currentSong = musicState.value.currentSong
        val songs =
            if (currentSong != null) {
                val index = musicState.value.playlist.indexOf(currentSong)
                musicState.value.playlist.toMutableList().apply { add(index + 1, song) }
            } else {
                musicState.value.playlist.toMutableList().apply { add(0, song) }
            }

        updatePlaylist(songs)

        return songs.indexOf(song)
    }

    fun addSongToLast(song: Song) {
        val songs = musicState.value.playlist.toMutableList().apply { add(song) }
        updatePlaylist(songs)
    }

    private fun updateCurrentSong() {
        val currentMediaId = controller?.currentMediaItem?.mediaId
        val currentSong = musicState.value.playlist.find { it.id.toString() == currentMediaId }

        _musicState.update { it.copy(currentSong = currentSong) }
    }
}
