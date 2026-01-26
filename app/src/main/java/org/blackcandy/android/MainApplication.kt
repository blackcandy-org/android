package org.blackcandy.android

import android.app.Application
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.defaultFragmentDestination
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.navigation.config.registerFragmentDestinations
import org.blackcandy.android.bridge.AccountComponent
import org.blackcandy.android.bridge.AlbumComponent
import org.blackcandy.android.bridge.FlashComponent
import org.blackcandy.android.bridge.PlaylistComponent
import org.blackcandy.android.bridge.SearchComponent
import org.blackcandy.android.bridge.SongsComponent
import org.blackcandy.android.bridge.ThemeComponent
import org.blackcandy.android.di.androidModule
import org.blackcandy.android.fragments.web.WebBottomSheetFragment
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.android.fragments.web.WebHomeFragment
import org.blackcandy.android.fragments.web.WebLibraryFragment
import org.blackcandy.shared.di.appModule
import org.blackcandy.shared.utils.BLACK_CANDY_USER_AGENT
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        configureApp()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule() + androidModule)
        }
    }

    private fun configureApp() {
        Hotwire.config.applicationUserAgentPrefix = "${BLACK_CANDY_USER_AGENT};"
        Hotwire.config.jsonConverter = KotlinXJsonConverter()
        Hotwire.defaultFragmentDestination = WebFragment::class

        Hotwire.loadPathConfiguration(
            context = this,
            location =
                PathConfiguration.Location(
                    assetFilePath = "json/configuration.json",
                ),
        )

        Hotwire.registerFragmentDestinations(
            WebFragment::class,
            WebHomeFragment::class,
            WebLibraryFragment::class,
            WebBottomSheetFragment::class,
        )

        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("account", ::AccountComponent),
            BridgeComponentFactory("search", ::SearchComponent),
            BridgeComponentFactory("album", ::AlbumComponent),
            BridgeComponentFactory("flash", ::FlashComponent),
            BridgeComponentFactory("playlist", ::PlaylistComponent),
            BridgeComponentFactory("songs", ::SongsComponent),
            BridgeComponentFactory("theme", ::ThemeComponent),
        )
    }
}
