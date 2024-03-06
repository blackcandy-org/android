package org.blackcandy.android.compose.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import org.blackcandy.android.R
import org.blackcandy.android.models.Song

@Composable
fun PlayerInfo(
    modifier: Modifier = Modifier,
    currentSong: Song?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        AsyncImage(
            model = currentSong?.albumImageUrl?.large,
            contentDescription = stringResource(R.string.album_cover),
            modifier =
                Modifier
                    .widthIn(max = dimensionResource(R.dimen.player_album_cover_max_size))
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)))
                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
            contentScale = ContentScale.FillWidth,
        )

        Text(
            currentSong?.name ?: stringResource(R.string.not_playing),
            modifier =
                Modifier
                    .padding(top = dimensionResource(R.dimen.padding_medium)),
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            currentSong?.artistName ?: "",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
