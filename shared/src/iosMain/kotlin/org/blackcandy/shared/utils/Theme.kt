package org.blackcandy.shared.utils

import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

actual fun updateAppTheme(theme: Theme) {
    val style =
        when (theme) {
            Theme.DARK -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
            Theme.LIGHT -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            Theme.AUTO -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
        }

    UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { it.windows }
        .filterIsInstance<UIWindow>()
        .forEach { it.overrideUserInterfaceStyle = style }
}
