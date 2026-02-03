package org.blackcandy.shared.utils

import platform.Foundation.NSHTTPCookie
import platform.Foundation.NSURL
import platform.WebKit.WKWebsiteDataStore

actual object Cookies {
    val dataStore: WKWebsiteDataStore = WKWebsiteDataStore.defaultDataStore()

    actual fun update(
        path: String,
        cookies: List<String>,
    ) {
        val cookieStore = dataStore.httpCookieStore
        val url = NSURL.URLWithString(path) ?: return

        cookies.forEach { cookieString ->
            val headerFields = mapOf<Any?, Any?>("Set-Cookie" to cookieString)
            val nsCookies = NSHTTPCookie.cookiesWithResponseHeaderFields(headerFields, url)
            nsCookies.forEach { cookie ->
                cookieStore.setCookie(cookie as NSHTTPCookie, completionHandler = null)
            }
        }
    }

    actual fun clean() {
        val cookieStore = dataStore.httpCookieStore

        cookieStore.getAllCookies { cookies ->
            cookies?.forEach { cookie ->
                cookieStore.deleteCookie(cookie as NSHTTPCookie, completionHandler = null)
            }
        }
    }
}
