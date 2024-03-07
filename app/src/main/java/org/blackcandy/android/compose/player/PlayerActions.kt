package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R
import org.blackcandy.android.models.PlaybackMode

@Composable
fun PlayerActions(
    modifier: Modifier = Modifier,
    playbackMode: PlaybackMode,
    onModeSwitchButtonClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        FilledIconToggleButton(
            checked = playbackMode != PlaybackMode.NO_REPEAT,
            onCheckedChange = { _ -> onModeSwitchButtonClicked() },
        ) {
            Icon(
                painter = painterResource(playbackMode.iconResourceId),
                contentDescription = stringResource(playbackMode.titleResourceId),
            )
        }

        IconToggleButton(
            checked = false,
            onCheckedChange = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_favorite_border_24),
                contentDescription = stringResource(R.string.unfavorited),
            )
        }

        FilledIconToggleButton(
            checked = false,
            onCheckedChange = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_format_list_bulleted_24),
                contentDescription = stringResource(R.string.playlist),
            )
        }
    }
}
