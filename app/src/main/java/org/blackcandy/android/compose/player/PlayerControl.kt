package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R

@Composable
fun PlayerControl(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Slider(value = 0.5f, onValueChange = {})

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Text(text = "00:00")
            Text(text = "02:00")
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_medium)),
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_previous_24),
                contentDescription = stringResource(R.string.previous),
                modifier =
                    Modifier
                        .size(dimensionResource(R.dimen.icon_size_large)),
            )

            Icon(
                painter = painterResource(R.drawable.baseline_play_arrow_24),
                contentDescription = stringResource(R.string.play),
                modifier =
                    Modifier
                        .size(dimensionResource(R.dimen.icon_size_large)),
            )

            Icon(
                painter = painterResource(R.drawable.baseline_skip_next_24),
                contentDescription = stringResource(R.string.next),
                modifier =
                    Modifier
                        .size(dimensionResource(R.dimen.icon_size_large)),
            )
        }
    }
}
