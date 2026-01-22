package org.blackcandy.android.fragments.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import org.blackcandy.android.databinding.FragmentWebHomeBinding
import org.blackcandy.shared.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@HotwireDestinationDeepLink(uri = "hotwire://fragment/web/home")
class WebHomeFragment : WebFragment() {
    private val viewModel: HomeViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentWebHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWebHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
