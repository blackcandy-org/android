package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.blackcandy.android.R
import org.blackcandy.android.utils.SnackbarUtil.Companion.ShowSnackbar
import org.blackcandy.android.viewmodels.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

enum class PlayerRoute {
    FullPlayer,
    Playlist,
}

@Composable
fun PlayerScreen(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: PlayerViewModel = koinViewModel(),
    windowSizeClass: WindowSizeClass,
) {
    val uiState by viewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute =
        PlayerRoute.valueOf(
            backStackEntry?.destination?.route ?: PlayerRoute.FullPlayer.name,
        )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentRoute == PlayerRoute.Playlist) {
                PlaylistAppBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    onClearAllButtonClicked = { viewModel.clearPlaylist() },
                )
            }
        },
        containerColor = Color.Transparent,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PlayerRoute.FullPlayer.name,
            modifier =
                Modifier
                    .nestedScroll(rememberNestedScrollInteropConnection()),
        ) {
            composable(route = PlayerRoute.FullPlayer.name) {
                FullPlayer(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                    windowSizeClass = windowSizeClass,
                    currentSong = uiState.musicState.currentSong,
                    isPlaying = uiState.musicState.isPlaying,
                    isLoading = uiState.musicState.isLoading,
                    currentPosition = uiState.currentPosition,
                    playbackMode = uiState.musicState.playbackMode,
                    onPreviousButtonClicked = { viewModel.previous() },
                    onNextButtonClicked = { viewModel.next() },
                    onPlayButtonClicked = { viewModel.play() },
                    onPauseButtonClicked = { viewModel.pause() },
                    onSeek = { viewModel.seekTo(it) },
                    onModeSwitchButtonClicked = { viewModel.nextMode() },
                    onFavoriteButtonClicked = { viewModel.toggleFavorite() },
                    onPlaylistButtonClicked = { navController.navigate(PlayerRoute.Playlist.name) },
                )
            }

            composable(route = PlayerRoute.Playlist.name) {
                Playlist(
                    modifier =
                        Modifier
                            .padding(innerPadding),
                    playlist = uiState.musicState.playlist,
                    currentSong = uiState.musicState.currentSong,
                    onItemClicked = { songId -> viewModel.playOn(songId) },
                    onItemSweepToDismiss = { songId -> viewModel.removeSongFromPlaylist(songId) },
                    onItemMoved = { from, to -> viewModel.moveSongInPlaylist(from, to) },
                )
            }
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
fun PlaylistAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onClearAllButtonClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.playing_queue)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_description),
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onClearAllButtonClicked) {
                Icon(
                    painter = painterResource(R.drawable.baseline_clear_all_24),
                    contentDescription = stringResource(R.string.clear_all),
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
    )
}
