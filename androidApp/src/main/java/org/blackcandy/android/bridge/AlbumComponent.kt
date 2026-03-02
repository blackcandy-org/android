package org.blackcandy.android.bridge

import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.Serializable
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.shared.viewmodels.WebViewModel

class AlbumComponent(
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
        val data = message.data<AlbumData>() ?: return
        viewModel.playAlbum(data.albumId)
    }

    private fun handlePlayBeginWithEvent(message: Message) {
        val data = message.data<AlbumData>() ?: return
        viewModel.playAlbumBeginWith(data.albumId, data.songId!!)
    }

    @Serializable
    data class AlbumData(
        val albumId: Long,
        val songId: Long?,
    )
}
