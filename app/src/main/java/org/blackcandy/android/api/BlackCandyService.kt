package org.blackcandy.android.api

import org.blackcandy.android.models.SystemInfo
import retrofit2.http.GET
import retrofit2.http.Headers

interface BlackCandyService {
    @Headers("Accept: application/json")
    @GET("system")
    suspend fun getSystemInfo(): SystemInfo
}
