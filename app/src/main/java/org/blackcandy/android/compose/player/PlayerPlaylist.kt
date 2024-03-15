package org.blackcandy.android.compose.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.blackcandy.android.R
import org.blackcandy.android.models.Song
import org.blackcandy.android.utils.DurationFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPlaylist(
    modifier: Modifier = Modifier,
    playlist: List<Song>,
    currentSong: Song?,
    onItemClicked: (Int) -> Unit,
    onItemSweepToDismiss: (Song) -> Unit,
) {
    if (playlist.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.empty_playlist_indication))
        }
    } else {
        LazyColumn(modifier = modifier) {
            itemsIndexed(playlist, key = { _, song -> song.id }) { index, song ->
                val dismissState =
                    rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            onItemSweepToDismiss(song)
                            true
                        },
                    )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.error)
                                    .padding(horizontal = dimensionResource(R.dimen.padding_small)),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_delete_24),
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.onError,
                            )
                        }
                    },
                ) {
                    ListItem(
                        modifier =
                            Modifier
                                .clickable(
                                    onClick = { onItemClicked(index) },
                                ),
                        headlineContent = {
                            Text(
                                text = song.name,
                                color = if (song == currentSong) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                fontWeight = if (song == currentSong) FontWeight.Bold else null,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        supportingContent = {
                            Text(
                                text = song.artistName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        trailingContent = { Text(text = DurationFormatter.string(song.duration)) },
                    )

                    HorizontalDivider()
                }
            }
        }
    }
}
