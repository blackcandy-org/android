package org.blackcandy.android

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import org.blackcandy.android.features.web.WebFragment
import org.blackcandy.android.features.web.WebHomeFragment
import kotlin.reflect.KClass

open class MainNavHostFragment : TurboSessionNavHostFragment() {
    override val sessionName = "main"
    override val startLocation = "http://10.0.2.2:3000"

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() = listOf(
            // Leave empty unless you have more
            // than one TurboActivity in your app
        )

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            WebHomeFragment::class,
            // And any other TurboFragments in your app
        )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json",
        )

    override fun onSessionCreated() {
        super.onSessionCreated()
        session.webView.settings.userAgentString = "Turbo Native Android"
    }
}
