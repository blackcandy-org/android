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
    private val preferencesDataSource: PreferencesDataSource,
    private val encryptedDataSource: EncryptedDataSource,
) {
    suspend fun login(
        email: String,
        password: String,
    ): TaskResult<String> {
        try {
            val response = service.createAuthentication(email, password).orThrow()
            val serverAddress = preferencesDataSource.getServerAddress()

            Cookies.update(serverAddress, response.cookies)
            preferencesDataSource.updateCurrentUser(response.user)
            encryptedDataSource.updateApiToken(response.token)

            // Clear previous cached auth token in http client
            httpClient
                .plugin(Auth)
                .providers
                .filterIsInstance<BearerAuthProvider>()
                .first()
                .clearToken()

            return TaskResult.Success(serverAddress)
        } catch (e: Exception) {
            return TaskResult.Failure(e.message)
        }
    }

    suspend fun logout() {
        service.removeAuthentication()
        encryptedDataSource.removeApiToken()
        Cookies.clean()
        preferencesDataSource.updateCurrentUser(null)
    }

    suspend fun getCurrentUser(): User? = preferencesDataSource.getCurrentUser()

    fun getCurrentUserFlow(): Flow<User?> = preferencesDataSource.getCurrentUserFlow()
}
