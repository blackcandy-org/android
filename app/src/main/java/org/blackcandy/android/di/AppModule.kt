package org.blackcandy.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.api.ServerAddressInterceptor
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.SystemInfoRepository
import org.blackcandy.android.viewmodels.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    factory { ServerAddressInterceptor(get()) }
    single { provideDataStore(androidContext()) }
    single { ServerAddressRepository(get()) }
    single { provideBlackCandyService(get()) }
    single { SystemInfoRepository(get()) }
    viewModel { LoginViewModel(get(), get()) }
}

private const val DATASTORE_PREFERENCES_NAME = "user_preferences"

private fun provideBlackCandyService(serverAddressInterceptor: ServerAddressInterceptor): BlackCandyService {
    val client = OkHttpClient.Builder()
        .addInterceptor(serverAddressInterceptor)
        .build()

    val contentType = "application/json".toMediaType()

    return Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(contentType))
        // Retrofit will get the base url from serverAddressInterceptor,
        // So this base url will be ignored. But Retrofit needs to get base url,
        // so add a fake url here.
        .baseUrl("http://foo.bar")
        .client(client)
        .build()
        .create(BlackCandyService::class.java)
}

private fun provideDataStore(appContext: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = { appContext.preferencesDataStoreFile(DATASTORE_PREFERENCES_NAME) },
    )
}
