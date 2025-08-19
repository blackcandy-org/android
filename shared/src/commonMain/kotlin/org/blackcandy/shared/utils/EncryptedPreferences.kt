package org.blackcandy.shared.utils

interface EncryptedPreferences {
    fun get(key: String): String?
    fun set(key: String, value: String)
    fun del(key: String)
}