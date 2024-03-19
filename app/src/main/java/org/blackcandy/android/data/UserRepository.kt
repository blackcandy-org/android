package org.blackcandy.android.data

import android.webkit.CookieManager
import androidx.datastore.core.DataStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.flow.Flow
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.User
import org.blackcandy.android.utils.TaskResult

class UserRepository(
    private val httpClient: HttpClient,
    private val service: BlackCandyService,
    private val cookieManager: CookieManager,
    private val userDataStore: DataStore<User?>,
    private val preferencesDataSource: PreferencesDataSource,
    private val encryptedPreferencesDataSource: EncryptedPreferencesDataSource,
) {
    suspend fun login(
        email: String,
        password: String,
    ): TaskResult<Unit> {
        try {
            val response = service.createAuthentication(email, password).orThrow()
            val serverAddress = preferencesDataSource.getServerAddress()

            response.cookies.forEach {
                cookieManager.setCookie(serverAddress, it)
            }

            cookieManager.flush()
            userDataStore.updateData { response.user }
            encryptedPreferencesDataSource.updateApiToken(response.token)

            // Clear previous cached auth token in http client
            httpClient.plugin(Auth).providers
                .filterIsInstance<BearerAuthProvider>()
                .first().clearToken()

            return TaskResult.Success(Unit)
        } catch (e: Exception) {
            return TaskResult.Failure(e.message)
        }
    }

    suspend fun logout() {
        service.removeAuthentication()
        encryptedPreferencesDataSource.removeApiToken()
        cookieManager.removeAllCookies(null)
        userDataStore.updateData { null }
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userDataStore.data
    }
}
