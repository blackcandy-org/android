package org.blackcandy.android.features.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import org.blackcandy.android.databinding.FragmentWebHomeBinding

@TurboNavGraphDestination(uri = "turbo://fragment/web/home")
class WebHomeFragment : WebFragment() {
    private var _binding: FragmentWebHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWebHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
