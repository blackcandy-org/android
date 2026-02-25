package org.blackcandy.shared.models

class Playlist {
    var isShuffled = false
    var orderedSongs: List<Song> = emptyList()

    private var shuffledSongs: List<Song> = emptyList()

    val songs get() = if (isShuffled) shuffledSongs else orderedSongs

    fun update(songs: List<Song>) {
        orderedSongs = songs.toMutableList()
        shuffledSongs = songs.toMutableList().apply { shuffle() }
    }

    fun updateSong(song: Song) {
        orderedSongs = orderedSongs.map { if (it.id == song.id) song else it }.toMutableList()
        shuffledSongs = shuffledSongs.map { if (it.id == song.id) song else it }.toMutableList()
    }

    fun remove(song: Song) {
        orderedSongs = orderedSongs.filter { it.id != song.id }.toMutableList()
        shuffledSongs = shuffledSongs.filter { it.id != song.id }.toMutableList()
    }

    fun insert(
        song: Song,
        index: Int,
    ) {
        orderedSongs = orderedSongs.toMutableList().apply { add(index, song) }
        shuffledSongs = shuffledSongs.toMutableList().apply { add(index, song) }
    }

    fun append(song: Song) {
        orderedSongs = orderedSongs.toMutableList().apply { add(song) }
        shuffledSongs = shuffledSongs.toMutableList().apply { add(song) }
    }

    fun move(
        from: Int,
        to: Int,
    ) {
        orderedSongs = orderedSongs.toMutableList().apply { add(to, removeAt(from)) }
    }

    fun clear() {
        orderedSongs = emptyList()
        shuffledSongs = emptyList()
    }
}
