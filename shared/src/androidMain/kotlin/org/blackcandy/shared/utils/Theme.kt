package org.blackcandy.shared.utils

import androidx.appcompat.app.AppCompatDelegate

actual fun updateAppTheme(theme: Theme) {
    when (theme) {
        Theme.DARK -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        Theme.LIGHT -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        Theme.AUTO -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
