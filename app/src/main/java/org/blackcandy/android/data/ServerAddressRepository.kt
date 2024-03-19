package org.blackcandy.android.data

import kotlinx.coroutines.flow.Flow

class ServerAddressRepository(
    private val preferencesDataSource: PreferencesDataSource,
) {
    suspend fun getServerAddress(): String {
        return preferencesDataSource.getServerAddress()
    }

    suspend fun updateServerAddress(serverAddress: String) {
        preferencesDataSource.updateServerAddress(serverAddress)
    }

    fun getServerAddressFlow(): Flow<String> {
        return preferencesDataSource.getServerAddressFlow()
    }
}
