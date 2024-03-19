package org.blackcandy.android.compose.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import org.blackcandy.android.R
import org.blackcandy.android.viewmodels.MiniPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MiniPlayer(viewModel: MiniPlayerViewModel = koinViewModel()) {
    val musicState by viewModel.musicState.collectAsState()

    Row(
        modifier =
            Modifier
                .height(dimensionResource(R.dimen.mini_player_height))
                .padding(horizontal = dimensionResource(R.dimen.padding_narrow)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(vertical = dimensionResource(R.dimen.padding_narrow))
                    .padding(start = dimensionResource(R.dimen.padding_small)),
        ) {
            if (musicState.hasCurrentSong) {
                AsyncImage(
                    model = musicState.currentSong!!.albumImageUrl.small,
                    contentDescription = stringResource(R.string.album_cover),
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_small))),
                )
            }

            Text(
                modifier =
                    Modifier
                        .basicMarquee(iterations = Int.MAX_VALUE)
                        .padding(start = dimensionResource(R.dimen.padding_narrow)),
                text = musicState.currentSong?.name ?: stringResource(R.string.not_playing),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Row(modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_narrow))) {
            IconButton(
                onClick = {
                    if (musicState.isPlaying) {
                        viewModel.pause()
                    } else {
                        viewModel.play()
                    }
                },
                enabled = musicState.hasCurrentSong,
            ) {
                if (musicState.isPlaying) {
                    Icon(
                        painterResource(R.drawable.baseline_pause_24),
                        contentDescription = stringResource(id = R.string.pause),
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.baseline_play_arrow_24),
                        contentDescription = stringResource(id = R.string.play),
                    )
                }
            }

            IconButton(
                onClick = { viewModel.next() },
                enabled = musicState.hasCurrentSong,
            ) {
                Icon(
                    painterResource(R.drawable.baseline_skip_next_24),
                    contentDescription = stringResource(id = R.string.next),
                )
            }
        }
    }
}
