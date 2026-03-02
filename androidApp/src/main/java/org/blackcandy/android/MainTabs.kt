package org.blackcandy.android

import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.tabs.HotwireBottomTab

fun buildMainTabs(serverAddress: String): List<HotwireBottomTab> =
    listOf(
        HotwireBottomTab(
            title = "Home",
            iconResId = R.drawable.baseline_home_24,
            configuration =
                NavigatorConfiguration(
                    name = "home",
                    startLocation = serverAddress,
                    navigatorHostId = R.id.home_container,
                ),
        ),
        HotwireBottomTab(
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
