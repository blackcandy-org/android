package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R
import org.blackcandy.android.viewmodels.MiniPlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MiniPlayer(viewModel: MiniPlayerViewModel = koinViewModel()) {
    Row(
        modifier =
            Modifier
                .height(dimensionResource(R.dimen.mini_player_height))
                .padding(horizontal = dimensionResource(R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            stringResource(R.string.not_playing),
            style = MaterialTheme.typography.labelLarge,
        )

        Row {
            IconButton(
                onClick = { viewModel.play() },
            ) {
                Icon(
                    painterResource(R.drawable.baseline_play_arrow_24),
                    contentDescription = stringResource(id = R.string.play),
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    painterResource(R.drawable.baseline_skip_next_24),
                    contentDescription = stringResource(id = R.string.next),
                )
            }
        }
    }
}
