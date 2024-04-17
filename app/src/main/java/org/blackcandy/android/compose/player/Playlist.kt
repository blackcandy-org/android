package org.blackcandy.android.compose.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R
import org.blackcandy.android.models.Song
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Playlist(
    modifier: Modifier = Modifier,
    playlist: List<Song>,
    currentSong: Song?,
    onItemClicked: (Int) -> Unit,
    onItemSweepToDismiss: (Int) -> Unit,
    onItemMoved: (Int, Int) -> Unit,
) {
    if (playlist.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.empty_playlist_indication))
        }
    } else {
        var fromIndex by remember { mutableIntStateOf(-1) }
        val (playlistState, setPlaylistState) = remember(playlist) { mutableStateOf(playlist) }
        val lazyListState = rememberLazyListState()
        val reorderableLazyColumnState =
            rememberReorderableLazyColumnState(lazyListState) { from, to ->
                setPlaylistState(
                    playlistState.toMutableList().apply {
                        add(to.index, removeAt(from.index))
                    },
                )
            }

        LazyColumn(
            modifier = modifier,
            state = lazyListState,
        ) {
            items(playlistState, key = { it.id }) { song ->
                ReorderableItem(reorderableLazyColumnState, key = song.id) {
                    PlaylistItem(
                        song = song,
                        isCurrent = song == currentSong,
                        scope = this,
                        onSweepToDismiss = onItemSweepToDismiss,
                        onClicked = onItemClicked,
                        onMoveStarted = { songId ->
                            fromIndex = playlistState.indexOfFirst { it.id == songId }
                        },
                        onMoveEnded = { songId ->
                            val toIndex = playlistState.indexOfFirst { it.id == songId }

                            if (fromIndex != -1 && fromIndex != toIndex) {
                                onItemMoved(fromIndex, toIndex)
                                fromIndex = -1
                            }
                        },
                    )
                }
            }
        }
    }
}
