package org.blackcandy.android.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.models.SystemInfo

interface BlackCandyService {
    suspend fun getSystemInfo(): SystemInfo
}

class BlackCandyServiceImpl(
    private val client: HttpClient,
    private val serverAddressRepository: ServerAddressRepository,
) : BlackCandyService {
    override suspend fun getSystemInfo(): SystemInfo {
        return client.get(apiUrl("/system")).body()
    }

    private suspend fun apiUrl(path: String): String {
        val serverAddress = serverAddressRepository.getServerAddress()
        return "$serverAddress/api/v1$path"
    }
}
