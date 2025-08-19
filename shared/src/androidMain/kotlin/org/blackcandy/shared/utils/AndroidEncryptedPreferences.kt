package org.blackcandy.shared.utils

import android.content.SharedPreferences

class AndroidEncryptedPreferences(
    private val encryptedSharedPrefs: SharedPreferences,
): EncryptedPreferences {
    override fun get(key: String): String? {
        return encryptedSharedPrefs.getString(key, null)
    }

    override fun set(key: String, value: String) {
        with(encryptedSharedPrefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun del(key: String) {
        with(encryptedSharedPrefs.edit()) {
            remove(key)
            apply()
        }
    }
}