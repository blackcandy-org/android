package org.blackcandy.android.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import org.blackcandy.android.models.Song

class MusicServiceController(
    private val appContext: Context,
) {
    private var controller: MediaController? = null

    fun initMediaController(onInitialized: () -> Unit) {
        val controllerFuture =
            MediaController.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, MusicService::class.java)),
            ).buildAsync()

        controllerFuture.addListener({
            controller = controllerFuture.get()
            onInitialized()
        }, MoreExecutors.directExecutor())
    }

    fun updatePlaylist(songs: List<Song>) {
        val mediaItems = songs.map { it.toMediaItem() }
        controller?.addMediaItems(mediaItems)
    }

    fun play() {
        controller?.play()
    }
}
