package org.blackcandy.shared.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.blackcandy.shared.data.EncryptedDataSource
import org.blackcandy.shared.media.MusicServiceController
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual val platformModule =
    module {
        single(named("PreferencesDataStore")) { provideDataStore() }
        single { EncryptedDataSource() }
        single { MusicServiceController(get()) }
    }

private const val DATASTORE_PREFERENCES_NAME = "user.preferences_pb"

@OptIn(ExperimentalForeignApi::class)
private fun provideDataStore(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val documentDirectory: NSURL? =
                NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )

            (requireNotNull(documentDirectory).path + "/$DATASTORE_PREFERENCES_NAME").toPath()
        },
    )
