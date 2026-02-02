package org.blackcandy.shared.data

actual class EncryptedDataSource {
    actual fun getApiToken(): String? = null

    actual fun updateApiToken(apiToken: String) {
    }

    actual fun removeApiToken() {
    }
}
