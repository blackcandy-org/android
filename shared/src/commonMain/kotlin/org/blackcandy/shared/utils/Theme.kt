package org.blackcandy.shared.utils

enum class Theme {
    DARK,
    LIGHT,
    AUTO,
}

expect fun updateAppTheme(theme: Theme)
