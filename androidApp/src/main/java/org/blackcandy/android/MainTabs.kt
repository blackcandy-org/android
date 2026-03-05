package org.blackcandy.android

import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.tabs.HotwireTab

fun buildMainTabs(serverAddress: String): List<HotwireTab> =
    listOf(
        HotwireTab(
            title = "Home",
            iconResId = R.drawable.baseline_home_24,
            configuration =
                NavigatorConfiguration(
                    name = "home",
                    startLocation = serverAddress,
                    navigatorHostId = R.id.home_container,
                ),
        ),
        HotwireTab(
            title = "Library",
            iconResId = R.drawable.baseline_library_music_24,
            configuration =
                NavigatorConfiguration(
                    name = "library",
                    startLocation = "$serverAddress/library",
                    navigatorHostId = R.id.library_container,
                ),
        ),
    )
