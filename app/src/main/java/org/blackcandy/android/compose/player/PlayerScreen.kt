package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.blackcandy.android.R
import org.blackcandy.android.utils.SnackbarUtil.Companion.ShowSnackbar
import org.blackcandy.android.viewmodels.PlayerUiState
import org.blackcandy.android.viewmodels.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: PlayerViewModel = koinViewModel(),
    windowSizeClass: WindowSizeClass,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (uiState.isPlaylistsVisible) {
                PlaylistAppBar(
                    onClearAllButtonClicked = { viewModel.clearPlaylist() },
                )
            }
        },
        containerColor = Color.Transparent,
    ) {
        if (
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact &&
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        ) {
            PlayerHorizontalLayout(
                modifier = Modifier.padding(it),
                uiState = uiState,
                viewModel = viewModel,
            )
        } else {
            PlayerVerticalLayout(
                modifier = Modifier.padding(it),
                uiState = uiState,
                viewModel = viewModel,
                isExpanded = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded,
            )
        }

        uiState.alertMessage?.let { alertMessage ->
            ShowSnackbar(alertMessage, snackbarHostState) {
                viewModel.alertMessageShown()
            }
        }
    }
}

@Composable
fun PlayerHorizontalLayout(
    modifier: Modifier,
    uiState: PlayerUiState,
    viewModel: PlayerViewModel,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayerArt(
                imageURL = uiState.musicState.currentSong?.albumImageUrl?.large,
                size = dimensionResource(R.dimen.player_album_cover_large_size),
                modifier =
                    Modifier
                        .padding(end = dimensionResource(R.dimen.padding_medium)),
            )

            Column {
                PlayerInfo(currentSong = uiState.musicState.currentSong)

                PlayerControl(
                    modifier =
                        Modifier
                            .widthIn(0.dp, dimensionResource(R.dimen.player_content_max_width))
                            .padding(top = dimensionResource(R.dimen.padding_small)),
                    isPlaying = uiState.musicState.isPlaying,
                    isLoading = uiState.musicState.isLoading,
                    currentPosition = uiState.currentPosition,
                    duration = uiState.musicState.currentSong?.duration ?: 0.0,
                    enabled = uiState.musicState.hasCurrentSong,
                    onPreviousButtonClicked = { viewModel.previous() },
                    onNextButtonClicked = { viewModel.next() },
                    onPlayButtonClicked = { viewModel.play() },
                    onPauseButtonClicked = { viewModel.pause() },
                    onSeek = { viewModel.seekTo(it) },
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PlayerActions(
            modifier =
                Modifier
                    .widthIn(0.dp, dimensionResource(R.dimen.player_content_max_width))
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .padding(horizontal = dimensionResource(R.dimen.padding_small)),
            playbackMode = uiState.musicState.playbackMode,
            isFavorited = uiState.musicState.currentSong?.isFavorited ?: false,
            isPlaylistVisible = uiState.isPlaylistsVisible,
            onModeSwitchButtonClicked = { viewModel.nextMode() },
            onFavoriteButtonClicked = { viewModel.toggleFavorite() },
            onPlaylistButtonToggled = { viewModel.setPlaylistVisibility(it) },
        )
    }
}

@Composable
fun PlayerVerticalLayout(
    modifier: Modifier,
    uiState: PlayerUiState,
    viewModel: PlayerViewModel,
    isExpanded: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxWidth()
                .nestedScroll(rememberNestedScrollInteropConnection()),
    ) {
        if (uiState.isPlaylistsVisible) {
            PlayerPlaylist(
                modifier = Modifier.weight(1f),
                playlist = uiState.musicState.playlist,
                currentSong = uiState.musicState.currentSong,
                onItemClicked = { songId -> viewModel.playOn(songId) },
                onItemSweepToDismiss = { songId -> viewModel.removeSongFromPlaylist(songId) },
                onItemMoved = { from, to -> viewModel.moveSongInPlaylist(from, to) },
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier =
                    Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PlayerArt(
                    imageURL = uiState.musicState.currentSong?.albumImageUrl?.large,
                    size = dimensionResource(if (isExpanded) R.dimen.player_album_cover_large_size else R.dimen.player_album_cover_size),
                )

                PlayerInfo(
                    currentSong = uiState.musicState.currentSong,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
                )

                PlayerControl(
                    modifier =
                        Modifier
                            .widthIn(0.dp, dimensionResource(R.dimen.player_content_max_width))
                            .padding(top = dimensionResource(R.dimen.padding_small)),
                    isPlaying = uiState.musicState.isPlaying,
                    isLoading = uiState.musicState.isLoading,
                    currentPosition = uiState.currentPosition,
                    duration = uiState.musicState.currentSong?.duration ?: 0.0,
                    enabled = uiState.musicState.hasCurrentSong,
                    onPreviousButtonClicked = { viewModel.previous() },
                    onNextButtonClicked = { viewModel.next() },
                    onPlayButtonClicked = { viewModel.play() },
                    onPauseButtonClicked = { viewModel.pause() },
                    onSeek = { viewModel.seekTo(it) },
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        PlayerActions(
            modifier =
                Modifier
                    .widthIn(0.dp, dimensionResource(R.dimen.player_content_max_width))
                    .padding(top = dimensionResource(R.dimen.padding_medium))
                    .padding(horizontal = dimensionResource(R.dimen.padding_small)),
            playbackMode = uiState.musicState.playbackMode,
            isFavorited = uiState.musicState.currentSong?.isFavorited ?: false,
            isPlaylistVisible = uiState.isPlaylistsVisible,
            onModeSwitchButtonClicked = { viewModel.nextMode() },
            onFavoriteButtonClicked = { viewModel.toggleFavorite() },
            onPlaylistButtonToggled = { viewModel.setPlaylistVisibility(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistAppBar(onClearAllButtonClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.playing_queue)) },
        actions = {
            IconButton(onClick = onClearAllButtonClicked) {
                Icon(
                    painter = painterResource(R.drawable.baseline_clear_all_24),
                    contentDescription = stringResource(R.string.clear_all),
                )
            }
        },
    )
}
