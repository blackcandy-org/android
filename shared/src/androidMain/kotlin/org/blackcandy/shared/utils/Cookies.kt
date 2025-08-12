package org.blackcandy.shared.utils

import android.webkit.CookieManager

actual object Cookies {
    val cookieManager: CookieManager = CookieManager.getInstance()

    actual fun update(path: String, cookies: List<String>) {
        cookies.forEach {
            cookieManager.setCookie(path, it)
        }

        cookieManager.flush()
    }

    actual fun clean() {
        cookieManager.removeAllCookies(null)
    }
}