package org.blackcandy.shared.data

import androidx.datastore.core.DataStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.flow.Flow
import org.blackcandy.shared.api.BlackCandyService
import org.blackcandy.shared.models.User
import org.blackcandy.shared.utils.Cookies
import org.blackcandy.shared.utils.TaskResult

class UserRepository(
    private val httpClient: HttpClient,
    private val service: BlackCandyService,
    private val cookies: Cookies,
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

            cookies.update(serverAddress, response.cookies)
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
        cookies.clean()
        userDataStore.updateData { null }
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userDataStore.data
    }
}
