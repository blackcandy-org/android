package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R

@Composable
fun MiniPlayer() {
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
            Icon(
                painterResource(R.drawable.baseline_play_arrow_24),
                contentDescription = stringResource(id = R.string.play),
                modifier =
                    Modifier
                        .padding(end = dimensionResource(R.dimen.padding_small)),
            )

            Icon(
                painterResource(R.drawable.baseline_skip_next_24),
                contentDescription = stringResource(id = R.string.next),
            )
        }
    }
}
