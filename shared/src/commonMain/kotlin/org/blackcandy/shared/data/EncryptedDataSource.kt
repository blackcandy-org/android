package org.blackcandy.shared.data

expect class EncryptedDataSource {
    fun getApiToken(): String?

    fun updateApiToken(apiToken: String)

    fun removeApiToken()
}
