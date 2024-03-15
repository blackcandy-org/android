package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import org.blackcandy.android.R
import org.blackcandy.android.utils.SnackbarUtil.Companion.ShowSnackbar
import org.blackcandy.android.viewmodels.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: PlayerViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(it)
                    .nestedScroll(rememberNestedScrollInteropConnection()),
        ) {
            if (uiState.isPlaylistsVisible) {
                PlayerPlaylist(
                    modifier = Modifier.weight(1f),
                    playlist = uiState.musicState.playlist,
                    currentSong = uiState.musicState.currentSong,
                    onItemClicked = { viewModel.playOn(it) },
                    onItemSweepToDismiss = { viewModel.removeSongFromPlaylist(it) },
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier =
                        Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PlayerInfo(currentSong = uiState.musicState.currentSong)
                    PlayerControl(
                        modifier =
                            Modifier
                                .padding(top = dimensionResource(R.dimen.padding_medium)),
                        isPlaying = uiState.musicState.isPlaying,
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
                        .padding(top = dimensionResource(R.dimen.padding_medium))
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                playbackMode = uiState.musicState.playbackMode,
                isFavorited = uiState.musicState.currentSong?.isFavorited ?: false,
                isPlaylistVisible = uiState.isPlaylistsVisible,
                onModeSwitchButtonClicked = { viewModel.nextMode() },
                onFavoriteButtonClicked = { viewModel.toggleFavorite() },
                onPlaylistButtonToggled = { viewModel.setPlaylistVisibility(it) },
            )
        }

        uiState.alertMessage?.let { alertMessage ->
            ShowSnackbar(alertMessage, snackbarHostState) {
                viewModel.alertMessageShown()
            }
        }
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
