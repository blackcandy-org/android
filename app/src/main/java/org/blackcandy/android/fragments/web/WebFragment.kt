package org.blackcandy.android.fragments.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.core.turbo.errors.HttpError
import dev.hotwire.core.turbo.errors.VisitError
import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import dev.hotwire.navigation.fragments.HotwireWebFragment
import org.blackcandy.android.R
import org.blackcandy.shared.viewmodels.WebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@HotwireDestinationDeepLink(uri = "hotwire://fragment/web")
open class WebFragment : HotwireWebFragment() {
    private val viewModel: WebViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_web, container, false)

    override fun onVisitErrorReceived(
        location: String,
        error: VisitError,
    ) {
        if (error is HttpError.ClientError.Unauthorized) {
            viewModel.logout()
        } else {
            super.onVisitErrorReceived(location, error)
        }
    }
}
