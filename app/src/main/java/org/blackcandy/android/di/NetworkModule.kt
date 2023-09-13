package org.blackcandy.android.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.api.ServerAddressInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideBlackCandyService(serverAddressInterceptor: ServerAddressInterceptor): BlackCandyService {
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
}
