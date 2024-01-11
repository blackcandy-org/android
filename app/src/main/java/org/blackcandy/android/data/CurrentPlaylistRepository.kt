package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.Song

class CurrentPlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSongs(): List<Song> {
        return service.getSongsFromCurrentPlaylist()
    }
}
