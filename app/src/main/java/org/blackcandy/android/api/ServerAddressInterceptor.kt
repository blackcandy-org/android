package org.blackcandy.android.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.blackcandy.android.data.ServerAddressRepository
import javax.inject.Inject

class ServerAddressInterceptor @Inject constructor(
    private val serverAddressRepository: ServerAddressRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val serverAddress: String = runBlocking {
            serverAddressRepository.getServerAddress()
        }

        val requestPath = request.url.encodedPath

        request = request.newBuilder()
            .url("$serverAddress/api/v1$requestPath")
            .build()

        return chain.proceed(request)
    }
}
