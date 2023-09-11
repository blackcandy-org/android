package org.blackcandy.android.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.SystemInfo
import retrofit2.Retrofit

class SystemInfoRepository {
    suspend fun getSystemInfo(serverAddress: String): SystemInfo {
        val apiAddress = "$serverAddress/api/v1/"
        val contentType = "application/json".toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
            .baseUrl(apiAddress)
            .build()

        val blackCandyService = retrofit.create(BlackCandyService::class.java)

        return blackCandyService.getSystemInfo()
    }
}
