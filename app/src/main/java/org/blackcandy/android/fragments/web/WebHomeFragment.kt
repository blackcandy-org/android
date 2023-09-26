package org.blackcandy.android.fragments.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import org.blackcandy.android.R
import org.blackcandy.android.databinding.FragmentWebHomeBinding
import org.blackcandy.android.viewmodels.WebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@TurboNavGraphDestination(uri = "turbo://fragment/web/home")
class WebHomeFragment : WebFragment() {
    private val viewModel: WebViewModel by viewModel()
    private var _binding: FragmentWebHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.top_bar_account -> {
                    navigate("${viewModel.serverAddress}/account")
                    true
                }

                else -> false
            }
        }
    }
}
