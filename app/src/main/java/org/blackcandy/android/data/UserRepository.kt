package org.blackcandy.android.data

import android.webkit.CookieManager
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.User

class UserRepository(
    private val service: BlackCandyService,
    private val cookieManager: CookieManager,
    private val serverAddressRepository: ServerAddressRepository,
    private val userDataStore: DataStore<User?>,
) {
    suspend fun authenticate(email: String, password: String) {
        val response = service.authenticate(email, password)
        val serverAddress = serverAddressRepository.getServerAddress()

        response.cookies.forEach {
            cookieManager.setCookie(serverAddress, it)
        }

        cookieManager.flush()

        userDataStore.updateData { response.user }
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userDataStore.data
    }

    suspend fun removeCurrentUser() {
        userDataStore.updateData { null }
    }

    fun removeUserCookies() {
        cookieManager.removeAllCookies(null)
    }
}
