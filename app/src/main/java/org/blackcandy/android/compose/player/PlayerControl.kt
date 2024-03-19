package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.blackcandy.android.R
import org.blackcandy.android.utils.DurationFormatter
import org.blackcandy.android.utils.NONE_DURATION_TEXT

@Composable
fun PlayerControl(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    isLoading: Boolean = false,
    currentPosition: Double = 0.0,
    duration: Double = 0.0,
    enabled: Boolean = true,
    onPreviousButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSeek: (Double) -> Unit,
) {
    val progressValue = if (duration > 0) (currentPosition / duration).toFloat() else 0f
    val currentPositionText = if (enabled) DurationFormatter.string(currentPosition) else NONE_DURATION_TEXT
    val durationText = if (enabled) DurationFormatter.string(duration) else NONE_DURATION_TEXT

    Column(
        modifier = modifier,
    ) {
        Slider(
            value = progressValue,
            enabled = enabled,
            onValueChange = {
                onSeek(duration * it)
            },
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Text(text = currentPositionText)
            Text(text = durationText)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_medium)),
        ) {
            IconButton(
                onClick = onPreviousButtonClicked,
                enabled = enabled,
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_skip_previous_24),
                    contentDescription = stringResource(R.string.previous),
                    modifier =
                        Modifier
                            .size(dimensionResource(R.dimen.icon_size_large)),
                )
            }

            IconButton(
                onClick = {
                    if (isPlaying) onPauseButtonClicked() else onPlayButtonClicked()
                },
                enabled = enabled,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large)),
                    )

                    return@IconButton
                }

                if (isPlaying) {
                    Icon(
                        painterResource(R.drawable.baseline_pause_24),
                        contentDescription = stringResource(R.string.pause),
                        modifier =
                            Modifier
                                .size(dimensionResource(R.dimen.icon_size_large)),
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.baseline_play_arrow_24),
                        contentDescription = stringResource(R.string.play),
                        modifier =
                            Modifier
                                .size(dimensionResource(R.dimen.icon_size_large)),
                    )
                }
            }

            IconButton(
                onClick = onNextButtonClicked,
                enabled = enabled,
            ) {
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
}
