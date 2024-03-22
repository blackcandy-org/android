package org.blackcandy.android.fragments.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import org.blackcandy.android.R
import org.blackcandy.android.viewmodels.WebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@TurboNavGraphDestination(uri = "turbo://fragment/web")
open class WebFragment : TurboWebFragment() {
    private val viewModel: WebViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onVisitErrorReceived(
        location: String,
        errorCode: Int,
    ) {
        if (errorCode == 401) {
            viewModel.logout()
        } else {
            super.onVisitErrorReceived(location, errorCode)
        }
    }
}
