package org.blackcandy.android.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
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
import org.blackcandy.android.models.AuthenticationResponse
import org.blackcandy.android.models.Song
import org.blackcandy.android.models.SystemInfo
import org.blackcandy.android.models.User

interface BlackCandyService {
    suspend fun getSystemInfo(): ApiResponse<SystemInfo>

    suspend fun createAuthentication(
        email: String,
        password: String,
    ): ApiResponse<AuthenticationResponse>

    suspend fun destroyAuthentication(): ApiResponse<Unit>

    suspend fun getSongsFromCurrentPlaylist(): ApiResponse<List<Song>>

    suspend fun addSongToFavorite(songId: Int): ApiResponse<Song>

    suspend fun deleteSongFromFavorite(songId: Int): ApiResponse<Song>
}

class BlackCandyServiceImpl(
    private val client: HttpClient,
) : BlackCandyService {
    override suspend fun getSystemInfo(): ApiResponse<SystemInfo> {
        return handleResponse {
            client.get("system").body()
        }
    }

    override suspend fun createAuthentication(
        email: String,
        password: String,
    ): ApiResponse<AuthenticationResponse> {
        return handleResponse {
            val response: HttpResponse =
                client.submitForm(
                    url = "authentication",
                    formParameters =
                        parameters {
                            append("with_cookie", "true")
                            append("session[email]", email)
                            append("session[password]", password)
                        },
                )

            val userElement = Json.parseToJsonElement(response.bodyAsText()).jsonObject["user"]!!

            val token = userElement.jsonObject["api_token"]?.jsonPrimitive.toString()
            val id = userElement.jsonObject["id"]?.jsonPrimitive?.int!!
            val userEmail = userElement.jsonObject["email"]?.jsonPrimitive.toString()
            val isAdmin = userElement.jsonObject["is_admin"]?.jsonPrimitive?.boolean!!
            val cookies = response.headers.getAll(HttpHeaders.SetCookie)!!

            AuthenticationResponse(
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
    }

    override suspend fun destroyAuthentication(): ApiResponse<Unit> {
        return handleResponse {
            client.delete("authentication").body()
        }
    }

    override suspend fun getSongsFromCurrentPlaylist(): ApiResponse<List<Song>> {
        return handleResponse {
            client.get("current_playlist/songs").body()
        }
    }

    override suspend fun addSongToFavorite(songId: Int): ApiResponse<Song> {
        return handleResponse {
            client.submitForm(
                url = "favorite_playlist/songs",
                formParameters =
                    parameters {
                        append("song_id", songId.toString())
                    },
            ).body()
        }
    }

    override suspend fun deleteSongFromFavorite(songId: Int): ApiResponse<Song> {
        return handleResponse {
            client.delete("favorite_playlist/songs/$songId").body()
        }
    }

    private suspend fun <T> handleResponse(request: suspend () -> T): ApiResponse<T> {
        try {
            return ApiResponse.Success(request())
        } catch (e: ApiException) {
            if (e.code == 204) {
                return ApiResponse.Success(Unit as T)
            }

            return ApiResponse.Failure(e)
        }
    }
}
