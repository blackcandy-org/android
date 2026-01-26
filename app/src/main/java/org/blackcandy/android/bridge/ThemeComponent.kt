package org.blackcandy.android.bridge

import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.Serializable
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.shared.utils.Theme
import org.blackcandy.shared.viewmodels.WebViewModel

class ThemeComponent(
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
            "initialize" -> handleInitializeEvent(message)
        }
    }

    private fun handleInitializeEvent(message: Message) {
        val data = message.data<ThemeData>() ?: return
        val themeValue = Theme.values().find { it.name == data.theme.uppercase() } ?: return

        viewModel.updateTheme(themeValue)
    }

    @Serializable
    data class ThemeData(
        val theme: String,
    )
}
