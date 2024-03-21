package org.blackcandy.android.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
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

    suspend fun removeAuthentication(): ApiResponse<Unit>

    suspend fun getSongsFromCurrentPlaylist(): ApiResponse<List<Song>>

    suspend fun addSongToFavorite(songId: Int): ApiResponse<Song>

    suspend fun removeSongFromFavorite(songId: Int): ApiResponse<Song>

    suspend fun removeAllSongsFromCurrentPlaylist(): ApiResponse<Unit>

    suspend fun removeSongFromCurrentPlaylist(songId: Int): ApiResponse<Unit>

    suspend fun moveSongInCurrentPlaylist(
        songId: Int,
        destinationSongId: Int,
    ): ApiResponse<Unit>

    suspend fun replaceCurrentPlaylistWithAlbumSongs(albumId: Int): ApiResponse<List<Song>>

    suspend fun replaceCurrentPlaylistWithPlaylistSongs(playlistId: Int): ApiResponse<List<Song>>

    suspend fun addSongToCurrentPlaylist(
        songId: Int,
        currentSongId: Int?,
        location: String?,
    ): ApiResponse<Song>
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

    override suspend fun removeAuthentication(): ApiResponse<Unit> {
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
            client.post("favorite_playlist/songs") {
                parameter("song_id", songId.toString())
            }.body()
        }
    }

    override suspend fun removeSongFromFavorite(songId: Int): ApiResponse<Song> {
        return handleResponse {
            client.delete("favorite_playlist/songs/$songId").body()
        }
    }

    override suspend fun removeAllSongsFromCurrentPlaylist(): ApiResponse<Unit> {
        return handleResponse {
            client.delete("current_playlist/songs").body()
        }
    }

    override suspend fun removeSongFromCurrentPlaylist(songId: Int): ApiResponse<Unit> {
        return handleResponse {
            client.delete("current_playlist/songs/$songId").body()
        }
    }

    override suspend fun moveSongInCurrentPlaylist(
        songId: Int,
        destinationSongId: Int,
    ): ApiResponse<Unit> {
        return handleResponse {
            client.put("current_playlist/songs/$songId/move") {
                parameter("destination_song_id", destinationSongId.toString())
            }.body()
        }
    }

    override suspend fun replaceCurrentPlaylistWithAlbumSongs(albumId: Int): ApiResponse<List<Song>> {
        return handleResponse {
            client.put("current_playlist/songs/albums/$albumId").body()
        }
    }

    override suspend fun replaceCurrentPlaylistWithPlaylistSongs(playlistId: Int): ApiResponse<List<Song>> {
        return handleResponse {
            client.put("current_playlist/songs/playlists/$playlistId").body()
        }
    }

    override suspend fun addSongToCurrentPlaylist(
        songId: Int,
        currentSongId: Int?,
        location: String?,
    ): ApiResponse<Song> {
        return handleResponse {
            client.post("current_playlist/songs") {
                parameter("song_id", songId.toString())

                if (currentSongId != null) {
                    parameter("current_song_id", currentSongId.toString())
                }

                if (location != null) {
                    parameter("location", location)
                }
            }.body()
        }
    }

    private suspend fun <T> handleResponse(request: suspend () -> T): ApiResponse<T> {
        return try {
            ApiResponse.Success(request())
        } catch (e: ApiException) {
            ApiResponse.Failure(e)
        }
    }
}
