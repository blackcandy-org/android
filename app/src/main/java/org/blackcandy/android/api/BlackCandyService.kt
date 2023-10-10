package org.blackcandy.android.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.models.AuthenticationResponse
import org.blackcandy.android.models.SystemInfo
import org.blackcandy.android.models.User

interface BlackCandyService {
    suspend fun getSystemInfo(): SystemInfo

    suspend fun authenticate(
        email: String,
        password: String,
    ): AuthenticationResponse
}

class BlackCandyServiceImpl(
    private val client: HttpClient,
    private val serverAddressRepository: ServerAddressRepository,
) : BlackCandyService {
    override suspend fun getSystemInfo(): SystemInfo {
        return client.get(apiUrl("/system")).body()
    }

    override suspend fun authenticate(
        email: String,
        password: String,
    ): AuthenticationResponse {
        val response: HttpResponse =
            client.submitForm(
                url = apiUrl("/authentication"),
                formParameters =
                    parameters {
                        append("with_session", "true")
                        append("user_session[email]", email)
                        append("user_session[password]", password)
                    },
            )

        val userElement = Json.parseToJsonElement(response.bodyAsText()).jsonObject["user"]!!

        val token = userElement.jsonObject["api_token"]?.jsonPrimitive.toString()
        val id = userElement.jsonObject["id"]?.jsonPrimitive?.int!!
        val userEmail = userElement.jsonObject["email"]?.jsonPrimitive.toString()
        val isAdmin = userElement.jsonObject["is_admin"]?.jsonPrimitive?.boolean!!
        val cookies = response.headers.getAll(HttpHeaders.SetCookie)!!

        return AuthenticationResponse(
            token = token,
            user =
                User(
                    id = id,
                    email = userEmail,
                    isAdmin = isAdmin,
                ),
            cookies = cookies,
        )
    }

    private suspend fun apiUrl(path: String): String {
        val serverAddress = serverAddressRepository.getServerAddress()
        return "$serverAddress/api/v1$path"
    }
}
