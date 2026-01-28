package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R
import org.blackcandy.shared.media.PlaybackMode

@Composable
fun PlayerActions(
    modifier: Modifier = Modifier,
    playbackMode: PlaybackMode,
    isFavorited: Boolean,
    onModeSwitchButtonClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    onPlaylistButtonClicked: () -> Unit,
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
            PlaybackModeIcon(playbackMode)
        }

        IconButton(
            onClick = onFavoriteButtonClicked,
        ) {
            if (isFavorited) {
                Icon(
                    painter = painterResource(R.drawable.baseline_favorite_24),
                    contentDescription = stringResource(R.string.favorited),
                    tint = Color.Red,
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.baseline_favorite_border_24),
                    contentDescription = stringResource(R.string.unfavorited),
                )
            }
        }

        IconButton(
            onClick = onPlaylistButtonClicked,
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_format_list_bulleted_24),
                contentDescription = stringResource(R.string.playlist),
            )
        }
    }
}

@Composable
fun PlaybackModeIcon(playbackMode: PlaybackMode) {
    val iconResourceId =
        when (playbackMode) {
            PlaybackMode.NO_REPEAT -> R.drawable.baseline_repeat_24
            PlaybackMode.REPEAT -> R.drawable.baseline_repeat_24
            PlaybackMode.REPEAT_ONE -> R.drawable.baseline_repeat_one_24
            PlaybackMode.SHUFFLE -> R.drawable.baseline_shuffle_24
        }

    val titleResourceId =
        when (playbackMode) {
            PlaybackMode.NO_REPEAT -> R.string.no_repeat_mode
            PlaybackMode.REPEAT -> R.string.repeat_mode
            PlaybackMode.REPEAT_ONE -> R.string.repeat_one_mode
            PlaybackMode.SHUFFLE -> R.string.shuffle_mode
        }

    Icon(
        painter = painterResource(iconResourceId),
        contentDescription = stringResource(titleResourceId),
    )
}
