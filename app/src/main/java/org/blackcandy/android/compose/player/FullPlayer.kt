package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import org.blackcandy.android.R
import org.blackcandy.android.models.PlaybackMode
import org.blackcandy.android.models.Song

@Composable
fun FullPlayer(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    currentSong: Song?,
    isPlaying: Boolean,
    isLoading: Boolean,
    currentPosition: Double,
    playbackMode: PlaybackMode,
    onPreviousButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSeek: (Double) -> Unit,
    onModeSwitchButtonClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    onPlaylistButtonClicked: () -> Unit,
) {
    if (
        windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact &&
        windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    ) {
        PlayerHorizontalLayout(
            modifier = modifier,
            currentSong = currentSong,
            isPlaying = isPlaying,
            isLoading = isLoading,
            currentPosition = currentPosition,
            playbackMode = playbackMode,
            onPreviousButtonClicked = onPreviousButtonClicked,
            onNextButtonClicked = onNextButtonClicked,
            onPlayButtonClicked = onPlayButtonClicked,
            onPauseButtonClicked = onPauseButtonClicked,
            onSeek = onSeek,
            onModeSwitchButtonClicked = onModeSwitchButtonClicked,
            onFavoriteButtonClicked = onFavoriteButtonClicked,
            onPlaylistButtonClicked = onPlaylistButtonClicked,
        )
    } else {
        PlayerVerticalLayout(
            modifier = modifier,
            currentSong = currentSong,
            isPlaying = isPlaying,
            isLoading = isLoading,
            currentPosition = currentPosition,
            playbackMode = playbackMode,
            onPreviousButtonClicked = onPreviousButtonClicked,
            onNextButtonClicked = onNextButtonClicked,
            onPlayButtonClicked = onPlayButtonClicked,
            onPauseButtonClicked = onPauseButtonClicked,
            onSeek = onSeek,
            onModeSwitchButtonClicked = onModeSwitchButtonClicked,
            onFavoriteButtonClicked = onFavoriteButtonClicked,
            onPlaylistButtonClicked = onPlaylistButtonClicked,
            isExpandedHeight = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded,
        )
    }
}

@Composable
fun PlayerHorizontalLayout(
    modifier: Modifier,
    currentSong: Song?,
    isPlaying: Boolean,
    isLoading: Boolean,
    currentPosition: Double,
    playbackMode: PlaybackMode,
    onPreviousButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSeek: (Double) -> Unit,
    onModeSwitchButtonClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    onPlaylistButtonClicked: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        PlayerArt(
            imageURL = currentSong?.albumImageUrl?.large,
            size = dimensionResource(R.dimen.player_album_cover_large_size),
            modifier =
                Modifier
                    .padding(end = dimensionResource(R.dimen.padding_medium)),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                PlayerInfo(currentSong = currentSong)

                PlayerControl(
                    modifier =
                        Modifier
                            .widthIn(max = dimensionResource(R.dimen.player_content_max_width))
                            .padding(top = dimensionResource(R.dimen.padding_narrow)),
                    isPlaying = isPlaying,
                    isLoading = isLoading,
                    currentPosition = currentPosition,
                    duration = currentSong?.duration ?: 0.0,
                    enabled = currentSong != null,
                    onPreviousButtonClicked = onPreviousButtonClicked,
                    onNextButtonClicked = onNextButtonClicked,
                    onPlayButtonClicked = onPlayButtonClicked,
                    onPauseButtonClicked = onPauseButtonClicked,
                    onSeek = onSeek,
                )
            }

            PlayerActions(
                modifier =
                    Modifier
                        .widthIn(max = dimensionResource(R.dimen.player_content_max_width))
                        .padding(top = dimensionResource(R.dimen.padding_narrow)),
                playbackMode = playbackMode,
                isFavorited = currentSong?.isFavorited ?: false,
                onModeSwitchButtonClicked = onModeSwitchButtonClicked,
                onFavoriteButtonClicked = onFavoriteButtonClicked,
                onPlaylistButtonClicked = onPlaylistButtonClicked,
            )
        }
    }
}

@Composable
fun PlayerVerticalLayout(
    modifier: Modifier,
    currentSong: Song?,
    isPlaying: Boolean,
    isLoading: Boolean,
    currentPosition: Double,
    playbackMode: PlaybackMode,
    onPreviousButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSeek: (Double) -> Unit,
    onModeSwitchButtonClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    onPlaylistButtonClicked: () -> Unit,
    isExpandedHeight: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_small)),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerArt(
                imageURL = currentSong?.albumImageUrl?.large,
                size = dimensionResource(if (isExpandedHeight) R.dimen.player_album_cover_large_size else R.dimen.player_album_cover_size),
            )

            PlayerInfo(
                currentSong = currentSong,
                center = true,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
            )

            PlayerControl(
                modifier =
                    Modifier
                        .widthIn(max = dimensionResource(R.dimen.player_content_max_width))
                        .padding(top = dimensionResource(R.dimen.padding_small)),
                isPlaying = isPlaying,
                isLoading = isLoading,
                largeIcon = true,
                currentPosition = currentPosition,
                duration = currentSong?.duration ?: 0.0,
                enabled = currentSong != null,
                onPreviousButtonClicked = onPreviousButtonClicked,
                onNextButtonClicked = onNextButtonClicked,
                onPlayButtonClicked = onPlayButtonClicked,
                onPauseButtonClicked = onPauseButtonClicked,
                onSeek = onSeek,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PlayerActions(
            modifier =
                Modifier
                    .widthIn(max = dimensionResource(R.dimen.player_content_max_width))
                    .padding(bottom = dimensionResource(R.dimen.padding_small), top = dimensionResource(R.dimen.padding_narrow)),
            playbackMode = playbackMode,
            isFavorited = currentSong?.isFavorited ?: false,
            onModeSwitchButtonClicked = onModeSwitchButtonClicked,
            onFavoriteButtonClicked = onFavoriteButtonClicked,
            onPlaylistButtonClicked = onPlaylistButtonClicked,
        )
    }
}
