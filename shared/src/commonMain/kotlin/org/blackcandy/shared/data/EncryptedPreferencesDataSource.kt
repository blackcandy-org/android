package org.blackcandy.shared.data

import org.blackcandy.shared.utils.EncryptedPreferences

class EncryptedPreferencesDataSource(
    private val encryptedPrefs: EncryptedPreferences,
) {
    companion object {
        private const val API_TOKEN_KEY = "api_token_key"
    }

    fun getApiToken(): String? {
        return encryptedPrefs.get(API_TOKEN_KEY)
    }

    fun updateApiToken(apiToken: String) {
        encryptedPrefs.set(API_TOKEN_KEY, apiToken)
    }

    fun removeApiToken() {
        encryptedPrefs.del(API_TOKEN_KEY)
    }
}
