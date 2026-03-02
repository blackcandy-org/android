package org.blackcandy.shared.data

import android.content.SharedPreferences

actual class EncryptedDataSource(
    private val encryptedSharedPrefs: SharedPreferences,
) {
    companion object {
        private const val API_TOKEN_KEY = "api_token_key"
    }

    actual fun getApiToken(): String? = encryptedSharedPrefs.getString(API_TOKEN_KEY, null)

    actual fun updateApiToken(apiToken: String) {
        with(encryptedSharedPrefs.edit()) {
            putString(API_TOKEN_KEY, apiToken)
            apply()
        }
    }

    actual fun removeApiToken() {
        with(encryptedSharedPrefs.edit()) {
            remove(API_TOKEN_KEY)
            apply()
        }
    }
}
