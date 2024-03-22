package org.blackcandy.android.di

import android.content.Context
import android.content.SharedPreferences
import android.webkit.CookieManager
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.blackcandy.android.api.ApiError
import org.blackcandy.android.api.ApiException
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.api.BlackCandyServiceImpl
import org.blackcandy.android.data.CurrentPlaylistRepository
import org.blackcandy.android.data.EncryptedPreferencesDataSource
import org.blackcandy.android.data.FavoritePlaylistRepository
import org.blackcandy.android.data.PreferencesDataSource
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.SystemInfoRepository
import org.blackcandy.android.data.UserRepository
import org.blackcandy.android.media.MusicServiceController
import org.blackcandy.android.models.User
import org.blackcandy.android.utils.BLACK_CANDY_USER_AGENT
import org.blackcandy.android.viewmodels.AccountSheetViewModel
import org.blackcandy.android.viewmodels.HomeViewModel
import org.blackcandy.android.viewmodels.LoginViewModel
import org.blackcandy.android.viewmodels.MainViewModel
import org.blackcandy.android.viewmodels.MiniPlayerViewModel
import org.blackcandy.android.viewmodels.NavHostViewModel
import org.blackcandy.android.viewmodels.PlayerViewModel
import org.blackcandy.android.viewmodels.WebViewModel
import org.chromium.net.CronetEngine
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.Executors

val appModule =
    module {
        single { provideJson() }
        single { provideCookieManager() }
        single { provideEncryptedSharedPreferences(androidContext()) }
        single(named("PreferencesDataStore")) { provideDataStore(androidContext()) }
        single(named("UserDataStore")) { provideUserDataStore(androidContext()) }
        single { provideHttpClient(get(), get(), get()) }
        single { provideDataSourceFactory(get(), get()) }

        single { PreferencesDataSource(get(named("PreferencesDataStore"))) }
        single { EncryptedPreferencesDataSource(get()) }

        single<BlackCandyService> { BlackCandyServiceImpl(get()) }
        single { MusicServiceController(androidContext()) }
        single { ServerAddressRepository(get()) }
        single { SystemInfoRepository(get()) }
        single { UserRepository(get(), get(), get(), get(named("UserDataStore")), get(), get()) }
        single { CurrentPlaylistRepository(get()) }
        single { FavoritePlaylistRepository(get()) }

        viewModel { LoginViewModel(get(), get(), get()) }
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { AccountSheetViewModel(get(), get()) }
        viewModel { NavHostViewModel(get(), get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { MiniPlayerViewModel(get()) }
        viewModel { PlayerViewModel(get(), get(), get()) }
        viewModel { WebViewModel(get()) }
    }

private const val DATASTORE_PREFERENCES_NAME = "user_preferences"
private const val USER_DATASTORE_FILE_NAME = "user.json"
private const val ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "encrypted_preferences.txt"

private fun provideHttpClient(
    json: Json,
    preferencesDataSource: PreferencesDataSource,
    encryptedPreferencesDataSource: EncryptedPreferencesDataSource,
): HttpClient {
    return HttpClient {
        expectSuccess = true

        install(UserAgent) {
            agent = BLACK_CANDY_USER_AGENT
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    encryptedPreferencesDataSource.getApiToken()?.let {
                        BearerTokens(it, "")
                    }
                }
            }
        }

        defaultRequest {
            val serverAddress =
                runBlocking {
                    preferencesDataSource.getServerAddress()
                }

            url("$serverAddress/api/v1/")
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is ClientRequestException -> {
                        val response = exception.response

                        val apiError =
                            try {
                                json.decodeFromString<ApiError>(response.bodyAsText())
                            } catch (e: Exception) {
                                null
                            }

                        throw ApiException(
                            code = response.status.value,
                            message = apiError?.message ?: exception.message,
                        )
                    }

                    else -> {
                        throw ApiException(
                            code = null,
                            message = exception.message,
                        )
                    }
                }
            }
        }
    }
}

private fun provideDataStore(appContext: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = { appContext.preferencesDataStoreFile(DATASTORE_PREFERENCES_NAME) },
    )
}

private fun provideUserDataStore(appContext: Context): DataStore<User?> {
    val serializer =
        object : Serializer<User?> {
            override val defaultValue: User?
                get() = null

            override suspend fun readFrom(input: InputStream): User? {
                return try {
                    Json.decodeFromString(
                        User.serializer(),
                        input.readBytes().decodeToString(),
                    )
                } catch (e: Exception) {
                    null
                }
            }

            override suspend fun writeTo(
                t: User?,
                output: OutputStream,
            ) {
                val data =
                    if (t == null) {
                        "{}".encodeToByteArray()
                    } else {
                        Json.encodeToString(User.serializer(), t).encodeToByteArray()
                    }

                withContext(Dispatchers.IO) {
                    output.write(data)
                }
            }
        }

    return DataStoreFactory.create(
        serializer = serializer,
        produceFile = { appContext.dataStoreFile(USER_DATASTORE_FILE_NAME) },
    )
}

private fun provideCookieManager(): CookieManager {
    return CookieManager.getInstance()
}

private fun provideEncryptedSharedPreferences(appContext: Context): SharedPreferences {
    return EncryptedSharedPreferences.create(
        ENCRYPTED_SHARED_PREFERENCES_FILE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        appContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
private fun provideDataSourceFactory(
    appContext: Context,
    encryptedPreferencesDataSource: EncryptedPreferencesDataSource,
): DataSource.Factory {
    val cronetEngine = CronetEngine.Builder(appContext).build()
    val apiToken = encryptedPreferencesDataSource.getApiToken()

    return DataSource.Factory {
        val dataSource =
            CronetDataSource.Factory(
                cronetEngine,
                Executors.newCachedThreadPool(),
            ).createDataSource()

        dataSource.setRequestProperty("Authorization", "Token $apiToken")

        dataSource
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun provideJson() =
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        namingStrategy = JsonNamingStrategy.SnakeCase
        useAlternativeNames = false
    }
