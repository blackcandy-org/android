package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
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
        containerColor = Color.Transparent,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(it)
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
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

            PlayerActions(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.padding_medium))
                        .padding(horizontal = dimensionResource(R.dimen.padding_small)),
                playbackMode = uiState.musicState.playbackMode,
                isFavorited = uiState.musicState.currentSong?.isFavorited ?: false,
                onModeSwitchButtonClicked = { viewModel.nextMode() },
                onFavoriteButtonClicked = { viewModel.toggleFavorite() },
            )
        }

        uiState.alertMessage?.let { alertMessage ->
            ShowSnackbar(alertMessage, snackbarHostState) {
                viewModel.alertMessageShown()
            }
        }
    }
}
