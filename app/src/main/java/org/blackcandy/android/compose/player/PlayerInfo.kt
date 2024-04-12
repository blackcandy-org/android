package org.blackcandy.android.compose.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import org.blackcandy.android.R
import org.blackcandy.android.models.Song

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerInfo(
    modifier: Modifier = Modifier,
    currentSong: Song?,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            currentSong?.name ?: stringResource(R.string.not_playing),
            modifier =
                Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_narrow))
                    .basicMarquee(iterations = Int.MAX_VALUE),
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            currentSong?.artistName ?: "",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
