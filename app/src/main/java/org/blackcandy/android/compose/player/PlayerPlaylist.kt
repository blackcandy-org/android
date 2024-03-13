package org.blackcandy.android.compose.player

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import org.blackcandy.android.models.Song
import org.blackcandy.android.utils.DurationFormatter

@Composable
fun PlayerPlaylist(
    modifier: Modifier = Modifier,
    playlist: List<Song>,
) {
    LazyColumn(modifier = modifier) {
        items(playlist, key = { it.id }) { song ->
            ListItem(
                headlineContent = {
                    Text(
                        text = song.name,
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
