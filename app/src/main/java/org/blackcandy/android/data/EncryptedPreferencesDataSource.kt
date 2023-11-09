package org.blackcandy.android.data

import android.content.SharedPreferences

class EncryptedPreferencesDataSource(
    private val encryptedSharedPrefs: SharedPreferences,
) {
    companion object {
        private const val API_TOKEN_KEY = "api_token_key"
    }

    fun getApiToken(): String? {
        return encryptedSharedPrefs.getString(API_TOKEN_KEY, null)
    }

    fun updateApiToken(apiToken: String) {
        with(encryptedSharedPrefs.edit()) {
            putString(API_TOKEN_KEY, apiToken)
            apply()
        }
    }

    fun removeApiToken() {
        with(encryptedSharedPrefs.edit()) {
            remove(API_TOKEN_KEY)
            apply()
        }
    }
}
