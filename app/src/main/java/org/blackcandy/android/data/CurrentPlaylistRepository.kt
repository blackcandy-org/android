package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.Song
import org.blackcandy.android.utils.TaskResult

class CurrentPlaylistRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSongs(): TaskResult<List<Song>> {
        return service.getSongsFromCurrentPlaylist().asResult()
    }
}
