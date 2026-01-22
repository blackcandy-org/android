package org.blackcandy.android.bridge

import androidx.fragment.app.Fragment
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.Serializable
import org.blackcandy.android.R

class SearchComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>,
) : BridgeComponent<HotwireDestination>(name, delegate) {
    private val fragment: Fragment
        get() = delegate.destination.fragment

    private val searchBar: SearchBar?
        get() = fragment.view?.findViewById(R.id.search_bar)

    private val searchView: SearchView?
        get() = fragment.view?.findViewById(R.id.search_view)

    override fun onReceive(message: Message) {
        when (message.event) {
            "connect" -> handleConnectEvent(message)
        }
    }

    private fun handleConnectEvent(message: Message) {
        searchView?.setupWithSearchBar(searchBar)
        searchView?.editText?.setOnEditorActionListener { _, _, _ ->
            val searchText = searchView?.text.toString()

            if (searchText.isEmpty()) return@setOnEditorActionListener false

            searchView?.hide()
            searchBar?.setText(searchText)
            replyTo("connect", SearchData(query = searchText))

            true
        }
    }

    @Serializable
    data class SearchData(
        val query: String,
    )
}
