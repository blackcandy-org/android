package org.blackcandy.shared.di

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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.blackcandy.shared.api.ApiError
import org.blackcandy.shared.api.ApiException
import org.blackcandy.shared.api.BlackCandyService
import org.blackcandy.shared.api.BlackCandyServiceImpl
import org.blackcandy.shared.data.CurrentPlaylistRepository
import org.blackcandy.shared.data.EncryptedDataSource
import org.blackcandy.shared.data.FavoritePlaylistRepository
import org.blackcandy.shared.data.PreferencesDataSource
import org.blackcandy.shared.data.ServerAddressRepository
import org.blackcandy.shared.data.SystemInfoRepository
import org.blackcandy.shared.data.UserRepository
import org.blackcandy.shared.utils.BLACK_CANDY_USER_AGENT
import org.blackcandy.shared.viewmodels.AccountSheetViewModel
import org.blackcandy.shared.viewmodels.HomeViewModel
import org.blackcandy.shared.viewmodels.LoginViewModel
import org.blackcandy.shared.viewmodels.MiniPlayerViewModel
import org.blackcandy.shared.viewmodels.MusicServiceViewModel
import org.blackcandy.shared.viewmodels.NavHostViewModel
import org.blackcandy.shared.viewmodels.PlayerViewModel
import org.blackcandy.shared.viewmodels.WebViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule =
    module {
        single { provideJson() }
        single { provideHttpClient(get(), get(), get()) }

        single { PreferencesDataSource(get(named("PreferencesDataStore"))) }
        single<BlackCandyService> { BlackCandyServiceImpl(get()) }
        single { ServerAddressRepository(get()) }
        single { SystemInfoRepository(get()) }
        single { UserRepository(get(), get(), get(named("UserDataStore")), get(), get()) }
        single { CurrentPlaylistRepository(get()) }
        single { FavoritePlaylistRepository(get()) }

        viewModel { LoginViewModel(get(), get(), get()) }
        viewModel { AccountSheetViewModel(get(), get()) }
        viewModel { NavHostViewModel(get(), get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { MiniPlayerViewModel(get()) }
        viewModel { PlayerViewModel(get(), get(), get()) }
        viewModel { WebViewModel(get()) }
        viewModel { MusicServiceViewModel(get(), get()) }
    }

private fun provideHttpClient(
    json: Json,
    preferencesDataSource: PreferencesDataSource,
    encryptedDataSource: EncryptedDataSource,
): HttpClient =
    HttpClient {
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
                    encryptedDataSource.getApiToken()?.let {
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

@OptIn(ExperimentalSerializationApi::class)
private fun provideJson() =
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        namingStrategy = JsonNamingStrategy.SnakeCase
        useAlternativeNames = false
    }
