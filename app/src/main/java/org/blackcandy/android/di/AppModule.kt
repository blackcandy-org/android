package org.blackcandy.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.blackcandy.android.api.ApiError
import org.blackcandy.android.api.ApiException
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.api.BlackCandyServiceImpl
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.SystemInfoRepository
import org.blackcandy.android.viewmodels.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.Exception

val appModule = module {
    single { provideJson() }
    single { provideDataStore(androidContext()) }
    single { provideHttpClient(get()) }
    single<BlackCandyService> { BlackCandyServiceImpl(get(), get()) }
    single { ServerAddressRepository(get()) }
    single { SystemInfoRepository(get()) }
    viewModel { LoginViewModel(get(), get()) }
}

private const val DATASTORE_PREFERENCES_NAME = "user_preferences"

private fun provideHttpClient(json: Json): HttpClient {
    return HttpClient() {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, request ->
                when (exception) {
                    is ClientRequestException -> {
                        val responseText = exception.response.bodyAsText()

                        val apiError = try {
                            json.decodeFromString(ApiError.serializer(), responseText)
                        } catch (e: Exception) {
                            null
                        }

                        throw ApiException(apiError?.message ?: exception.message)
                    }

                    else -> {
                        throw ApiException(exception.message)
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

private fun provideJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}
