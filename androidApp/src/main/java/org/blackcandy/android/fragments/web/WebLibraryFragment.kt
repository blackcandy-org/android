package org.blackcandy.android.fragments.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import org.blackcandy.android.databinding.FragmentWebLibraryBinding

@HotwireDestinationDeepLink(uri = "hotwire://fragment/web/library")
open class WebLibraryFragment : WebFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
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
}
