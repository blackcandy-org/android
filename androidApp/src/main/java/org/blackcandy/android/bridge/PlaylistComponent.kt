package org.blackcandy.android.bridge

import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.Serializable
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.shared.viewmodels.WebViewModel

class PlaylistComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>,
) : BridgeComponent<HotwireDestination>(name, delegate) {
    private val viewModel: WebViewModel
        get() {
            val fragment = delegate.destination.fragment as WebFragment
            return fragment.viewModel
        }

    override fun onReceive(message: Message) {
        when (message.event) {
            "play" -> handlePlayEvent(message)
            "playBeginWith" -> handlePlayBeginWithEvent(message)
        }
    }

    private fun handlePlayEvent(message: Message) {
        val data = message.data<PlaylistData>() ?: return
        viewModel.playPlaylist(data.playlistId)
    }

    private fun handlePlayBeginWithEvent(message: Message) {
        val data = message.data<PlaylistData>() ?: return
        viewModel.playPlaylistBeginWith(data.playlistId, data.songId!!)
    }

    @Serializable
    data class PlaylistData(
        val playlistId: Long,
        val songId: Long?,
    )
}
