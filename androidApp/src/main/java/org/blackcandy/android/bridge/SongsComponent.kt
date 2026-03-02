package org.blackcandy.android.bridge

import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.Serializable
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.shared.viewmodels.WebViewModel

class SongsComponent(
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
            "playNow" -> handlePlayNowEvent(message)
            "playNext" -> handlePlayNextEvent(message)
            "playLast" -> handlePlayLastEvent(message)
        }
    }

    private fun handlePlayNowEvent(message: Message) {
        val data = message.data<SongsData>() ?: return
        viewModel.playNow(data.songId)
    }

    private fun handlePlayNextEvent(message: Message) {
        val data = message.data<SongsData>() ?: return
        viewModel.playNext(data.songId)
    }

    private fun handlePlayLastEvent(message: Message) {
        val data = message.data<SongsData>() ?: return
        viewModel.playLast(data.songId)
    }

    @Serializable
    data class SongsData(
        val songId: Long,
    )
}
