package org.blackcandy.android.data

import android.webkit.CookieManager
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.User

class UserRepository(
    private val service: BlackCandyService,
    private val cookieManager: CookieManager,
    private val userDataStore: DataStore<User?>,
    private val preferencesDataSource: PreferencesDataSource,
    private val encryptedPreferencesDataSource: EncryptedPreferencesDataSource,
) {
    suspend fun login(
        email: String,
        password: String,
    ) {
        val response = service.createAuthentication(email, password)
        val serverAddress = preferencesDataSource.getServerAddress()

        response.cookies.forEach {
            cookieManager.setCookie(serverAddress, it)
        }

        cookieManager.flush()
        userDataStore.updateData { response.user }
        encryptedPreferencesDataSource.updateApiToken(response.token)
    }

    suspend fun logout() {
        service.destroyAuthentication()
        userDataStore.updateData { null }
        cookieManager.removeAllCookies(null)
        encryptedPreferencesDataSource.removeApiToken()
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userDataStore.data
    }
}
