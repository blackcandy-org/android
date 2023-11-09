package org.blackcandy.android.fragments.navs

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import org.blackcandy.android.fragments.sheets.AccountSheetFragment
import org.blackcandy.android.fragments.web.WebBottomSheetFragment
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.android.fragments.web.WebHomeFragment
import org.blackcandy.android.fragments.web.WebLibraryFragment
import org.blackcandy.android.utils.BLACK_CANDY_USER_AGENT
import org.blackcandy.android.viewmodels.NavHostViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

open class MainNavHostFragment : TurboSessionNavHostFragment() {
    private val viewModel: NavHostViewModel by viewModel()

    override val sessionName = "main"
    override val startLocation get() = viewModel.serverAddress

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() =
            listOf(
                // Leave empty unless you have more
                // than one TurboActivity in your app
            )

    override val registeredFragments: List<KClass<out Fragment>>
        get() =
            listOf(
                WebFragment::class,
                WebHomeFragment::class,
                WebLibraryFragment::class,
                WebBottomSheetFragment::class,
                AccountSheetFragment::class,
                // And any other TurboFragments in your app
            )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() =
            TurboPathConfiguration.Location(
                assetFilePath = "json/configuration.json",
            )

    override fun onSessionCreated() {
        super.onSessionCreated()
        session.webView.settings.userAgentString = BLACK_CANDY_USER_AGENT
    }
}
