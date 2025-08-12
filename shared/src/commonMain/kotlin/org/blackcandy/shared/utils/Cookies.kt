package org.blackcandy.shared.utils

expect object Cookies {
    fun update(path: String, cookies: List<String>): Unit
    fun clean(): Unit
}