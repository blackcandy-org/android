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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    largeIcon: Boolean = false,
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
    var progressValueState by remember { mutableFloatStateOf(0f) }

    val currentPositionText = if (enabled) DurationFormatter.string(currentPosition) else NONE_DURATION_TEXT
    val durationText = if (enabled) DurationFormatter.string(duration) else NONE_DURATION_TEXT

    Column(
        modifier = modifier,
    ) {
        Slider(
            value = if (progressValueState == 0f) progressValue else progressValueState,
            enabled = enabled,
            onValueChange = {
                progressValueState = it
            },
            onValueChangeFinished = {
                onSeek(duration * progressValueState)
                progressValueState = 0f
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
            val iconSize = dimensionResource(if (largeIcon) R.dimen.icon_size_large else R.dimen.icon_size_medium)

            IconButton(
                onClick = onPreviousButtonClicked,
                enabled = enabled,
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_skip_previous_24),
                    contentDescription = stringResource(R.string.previous),
                    modifier =
                        Modifier
                            .size(iconSize),
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
                        modifier =
                            Modifier.size(iconSize),
                    )

                    return@IconButton
                }

                if (isPlaying) {
                    Icon(
                        painterResource(R.drawable.baseline_pause_24),
                        contentDescription = stringResource(R.string.pause),
                        modifier =
                            Modifier
                                .size(iconSize),
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.baseline_play_arrow_24),
                        contentDescription = stringResource(R.string.play),
                        modifier =
                            Modifier
                                .size(iconSize),
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
                            .size(iconSize),
                )
            }
        }
    }
}
