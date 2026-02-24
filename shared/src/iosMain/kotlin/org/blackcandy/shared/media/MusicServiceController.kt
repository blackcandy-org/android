package org.blackcandy.shared.media

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.blackcandy.shared.data.EncryptedDataSource
import org.blackcandy.shared.models.Song
import org.blackcandy.shared.utils.BLACK_CANDY_USER_AGENT
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerTimeControlStatusPaused
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.Foundation.addObserver
import platform.darwin.NSObject
import platform.darwin.NSObjectProtocol
import platform.foundation.NSKeyValueObservingProtocol

@OptIn(ExperimentalForeignApi::class)
actual class MusicServiceController(
    private val encryptedDataSource: EncryptedDataSource,
) {
    private lateinit var player: AVPlayer
    private lateinit var apiToken: String
    private val _musicState = MutableStateFlow(MusicState())
    private var _currentPosition = MutableStateFlow(0.0)
    private var statusObserver: NSObject? = null
    private var playToEndObserver: NSObjectProtocol? = null
    private var timeObserver: Any? = null

    private val hasCurrentItem get() = player.currentItem !== null

    private val currentIndex: Int
        get() {
            val currentSong = musicState.value.currentSong ?: return 0
            return getSongIndex(currentSong.id)
        }

    actual val musicState = _musicState.asStateFlow()
    actual val currentPosition: Flow<Double> = _currentPosition

    actual fun initMediaController(onInitialized: () -> Unit) {
        AudioSessionController.setup(this)
        MediaRemoteController.setup(this)

        player = AVPlayer()
        apiToken = encryptedDataSource.getApiToken()!!

        setupStatusObservers()
        onInitialized()
    }

    actual fun updatePlaylist(songs: List<Song>) {
        songs.firstOrNull()?.let { song ->
            _musicState.update { it.copy(currentSong = song) }
        }

        _musicState.update { it.copy(playlist = songs) }
    }

    actual fun play() {
        if (hasCurrentItem) {
            player.play()
        } else {
            playOn(currentIndex)
        }
    }

    actual fun pause() {
        player.pause()
    }

    actual fun next() {
        playOn(currentIndex + 1)
    }

    actual fun previous() {
        playOn(currentIndex - 1)
    }

    actual fun playOn(index: Int) {
        val song = musicState.value.playlist.getOrNull(index) ?: return

        _musicState.update { it.copy(currentSong = song) }

        val asset =
            AVURLAsset(
                uRL = NSURL(string = song.url),
                options =
                    mapOf(
                        "AVURLAssetHTTPHeaderFieldsKey" to
                            mapOf(
                                "Authorization" to "Token $apiToken",
                                "User-Agent" to BLACK_CANDY_USER_AGENT,
                            ),
                    ),
            )

        val item = AVPlayerItem(asset)

        player.pause()
        player.replaceCurrentItemWithPlayerItem(item)
        player.play()
    }

    actual fun seekTo(seconds: Double) {
        player.seekToTime(CMTimeMakeWithSeconds(seconds, 1))
    }

    actual fun clearPlaylist() {
        updatePlaylist(emptyList())
    }

    actual fun deleteSongFromPlaylist(song: Song) {
        val songs =
            musicState.value.playlist
                .toMutableList()
                .apply { remove(song) }
        updatePlaylist(songs)
    }

    actual fun updateSongInPlaylist(song: Song) {
        val songs = musicState.value.playlist.map { if (it.id == song.id) song else it }
        updatePlaylist(songs)

        if (song.id == musicState.value.currentSong?.id) {
            updateCurrentSong()
        }
    }

    actual fun moveSongInPlaylist(
        from: Int,
        to: Int,
    ) {
        val songs =
            musicState.value.playlist
                .toMutableList()
                .apply { add(to, removeAt(from)) }
        updatePlaylist(songs)
    }

    actual fun setPlaybackMode(playbackMode: PlaybackMode) {
        _musicState.update { it.copy(playbackMode = playbackMode) }
    }

    actual fun getSongIndex(songId: Int): Int = musicState.value.playlist.indexOfFirst { it.id == songId }

    actual fun addSongToNext(song: Song): Int {
        val currentSong = musicState.value.currentSong
        val songs =
            if (currentSong != null) {
                val index = musicState.value.playlist.indexOf(currentSong)
                musicState.value.playlist
                    .toMutableList()
                    .apply { add(index + 1, song) }
            } else {
                musicState.value.playlist
                    .toMutableList()
                    .apply { add(0, song) }
            }

        updatePlaylist(songs)

        return songs.indexOf(song)
    }

    actual fun addSongToLast(song: Song) {
        val songs =
            musicState.value.playlist
                .toMutableList()
                .apply { add(song) }
        updatePlaylist(songs)
    }

    private fun stop() {
        seekTo(0.0)
        player.pause()
        player.replaceCurrentItemWithPlayerItem(null)
    }

    private fun replay() {
        seekTo(0.0)
        player.play()
    }

    private fun updateCurrentSong() {
        val currentSong = musicState.value.playlist.find { it.id == musicState.value.currentSong?.id }
        _musicState.update { it.copy(currentSong = currentSong) }
    }

    private fun setupStatusObservers() {
        statusObserver =
            object : NSObject(), NSKeyValueObservingProtocol {
                override fun observeValueForKeyPath(
                    keyPath: String?,
                    ofObject: Any?,
                    change: Map<Any?, *>?,
                    context: COpaquePointer?,
                ) {
                    when (player.timeControlStatus) {
                        AVPlayerTimeControlStatusPaused -> {
                            _musicState.update { it.copy(playbackState = PlaybackState.PAUSED) }
                        }

                        AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate -> {
                            _musicState.update { it.copy(playbackState = PlaybackState.BUFFERING) }
                        }

                        AVPlayerTimeControlStatusPlaying -> {
                            _musicState.update { it.copy(playbackState = PlaybackState.PLAYING) }
                        }
                    }
                }
            }

        player.addObserver(
            statusObserver!!,
            forKeyPath = "timeControlStatus",
            options = NSKeyValueObservingOptionNew,
            context = null,
        )

        timeObserver =
            player.addPeriodicTimeObserverForInterval(
                interval = CMTimeMake(value = 1, timescale = 1),
                queue = null,
                usingBlock = { time ->
                    _currentPosition.value = CMTimeGetSeconds(time)
                },
            )

        playToEndObserver =
            NSNotificationCenter.defaultCenter.addObserverForName(
                name = AVPlayerItemDidPlayToEndTimeNotification,
                `object` = null,
                queue = NSOperationQueue.mainQueue,
                usingBlock = { _ ->
                    _musicState.update { it.copy(playbackState = PlaybackState.ENDED) }

                    when (musicState.value.playbackMode) {
                        PlaybackMode.NO_REPEAT -> {
                            if (currentIndex == musicState.value.playlist.size - 1) {
                                stop()
                                _musicState.update { it.copy(currentSong = musicState.value.playlist.firstOrNull()) }
                            } else {
                                next()
                            }
                        }

                        PlaybackMode.REPEAT_ONE -> {
                            replay()
                        }

                        PlaybackMode.REPEAT -> {
                            if (currentIndex == musicState.value.playlist.size - 1) {
                                playOn(0)
                            }
                        }

                        else -> {
                            next()
                        }
                    }
                },
            )
    }
}
