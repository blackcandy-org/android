package org.blackcandy.android.data

class ServerAddressRepository(
    private val preferencesDataSource: PreferencesDataSource,
) {
    suspend fun getServerAddress(): String {
        return preferencesDataSource.getServerAddress()
    }

    suspend fun updateServerAddress(serverAddress: String) {
        preferencesDataSource.updateServerAddress(serverAddress)
    }
}
