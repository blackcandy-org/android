package org.blackcandy.android.fragments.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import org.blackcandy.android.databinding.FragmentWebLibraryBinding

@TurboNavGraphDestination(uri = "turbo://fragment/web/library")
open class WebLibraryFragment : WebFragment() {
    private var _binding: FragmentWebLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWebLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val searchText = binding.searchView.text.toString()

            if (searchText.isEmpty()) return@setOnEditorActionListener false

            binding.searchView.hide()
            binding.searchBar.setText(searchText)
            session.webView.evaluateJavascript("App.nativeBridge.search('$searchText')", null)

            true
        }
    }
}
