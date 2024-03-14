package org.blackcandy.android.compose.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.blackcandy.android.models.Song
import org.blackcandy.android.utils.DurationFormatter

@Composable
fun PlayerPlaylist(
    modifier: Modifier = Modifier,
    playlist: List<Song>,
    currentSong: Song?,
    onItemClicked: (Int) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(playlist, key = { _, song -> song.id }) { index, song ->
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
