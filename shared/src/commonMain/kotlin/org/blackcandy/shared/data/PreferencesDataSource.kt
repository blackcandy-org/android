package org.blackcandy.shared.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.blackcandy.shared.models.User

class PreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val SERVER_ADDRESS_KEY = stringPreferencesKey("server_address")
        private val CURRENT_USER_KEY = stringPreferencesKey("current_user")
    }

    suspend fun getServerAddress(): String = dataStore.data.first()[SERVER_ADDRESS_KEY] ?: ""

    suspend fun getCurrentUser(): User? = dataStore.data.first()[CURRENT_USER_KEY]?.let { Json.decodeFromString(it) }

    suspend fun updateServerAddress(serverAddress: String) {
        dataStore.edit { it[SERVER_ADDRESS_KEY] = serverAddress }
    }

    suspend fun updateCurrentUser(user: User?) {
        dataStore.edit { it[CURRENT_USER_KEY] = Json.encodeToString(user) }
    }

    fun getServerAddressFlow(): Flow<String> = dataStore.data.map { it[SERVER_ADDRESS_KEY] ?: "" }

    fun getCurrentUserFlow(): Flow<User?> = dataStore.data.map { it[CURRENT_USER_KEY]?.let { Json.decodeFromString(it) } }
}
