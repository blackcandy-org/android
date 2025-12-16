package org.blackcandy.shared.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.blackcandy.shared.data.EncryptedDataSource
import org.blackcandy.shared.media.MusicServiceController
import org.blackcandy.shared.models.User
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.InputStream
import java.io.OutputStream

actual val platformModule =
    module {
        single { provideEncryptedSharedPreferences(androidContext()) }
        single(named("PreferencesDataStore")) { provideDataStore(androidContext()) }
        single(named("UserDataStore")) { provideUserDataStore(androidContext()) }
        single { provideDataSourceFactory(get()) }

        single { EncryptedDataSource(get()) }
        single { MusicServiceController(androidContext()) }
    }

private const val DATASTORE_PREFERENCES_NAME = "user_preferences"
private const val USER_DATASTORE_FILE_NAME = "user.json"
private const val ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "encrypted_preferences.txt"

private fun provideDataStore(appContext: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        produceFile = { appContext.preferencesDataStoreFile(DATASTORE_PREFERENCES_NAME) },
    )

private fun provideUserDataStore(appContext: Context): DataStore<User?> {
    val serializer =
        object : Serializer<User?> {
            override val defaultValue: User?
                get() = null

            override suspend fun readFrom(input: InputStream): User? =
                try {
                    Json.decodeFromString(
                        User.serializer(),
                        input.readBytes().decodeToString(),
                    )
                } catch (e: Exception) {
                    null
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

private fun provideEncryptedSharedPreferences(appContext: Context): SharedPreferences =
    EncryptedSharedPreferences.create(
        ENCRYPTED_SHARED_PREFERENCES_FILE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        appContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

@androidx.annotation.OptIn(UnstableApi::class)
private fun provideDataSourceFactory(encryptedDataSource: EncryptedDataSource): DataSource.Factory {
    val httpClient = OkHttpClient().newBuilder().build()
    val apiToken = encryptedDataSource.getApiToken()

    return DataSource.Factory {
        val dataSource =
            OkHttpDataSource.Factory(httpClient).createDataSource()

        dataSource.setRequestProperty("Authorization", "Token $apiToken")

        dataSource
    }
}
