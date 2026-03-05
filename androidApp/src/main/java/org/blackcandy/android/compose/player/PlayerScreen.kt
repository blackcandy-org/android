package org.blackcandy.android.compose.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.blackcandy.android.R
import org.blackcandy.android.utils.SnackbarUtil.Companion.ShowSnackbar
import org.blackcandy.shared.viewmodels.PlayerViewModel
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
    val isWideLayout =
        windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact &&
            windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact

    if (isWideLayout) {
        PlayerScreenWideLayout(
            snackbarHostState = snackbarHostState,
            viewModel = viewModel,
            windowSizeClass = windowSizeClass,
            uiState = uiState,
        )
    } else {
        PlayerScreenCompactLayout(
            navController = navController,
            snackbarHostState = snackbarHostState,
            viewModel = viewModel,
            windowSizeClass = windowSizeClass,
            uiState = uiState,
        )
    }
}

@Composable
fun PlayerScreenWideLayout(
    snackbarHostState: SnackbarHostState,
    viewModel: PlayerViewModel,
    windowSizeClass: WindowSizeClass,
    uiState: org.blackcandy.shared.viewmodels.PlayerUiState,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
    ) { innerPadding ->
        Row(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        ) {
            FullPlayer(
                modifier =
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                windowSizeClass = windowSizeClass,
                inWideLayout = true,
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
                onPlaylistButtonClicked = null,
            )

            Column(
                modifier = Modifier.weight(1f),
            ) {
                PlaylistHeader(
                    tracksCount = uiState.musicState.playlist.size,
                    onClearAllButtonClicked = { viewModel.clearPlaylist() },
                )

                Playlist(
                    modifier =
                        Modifier
                            .heightIn(max = dimensionResource(R.dimen.playlist_max_height)),
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

@Composable
fun PlayerScreenCompactLayout(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: PlayerViewModel,
    windowSizeClass: WindowSizeClass,
    uiState: org.blackcandy.shared.viewmodels.PlayerUiState,
) {
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
                    inCompactHeight =
                        windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact &&
                            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact,
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

@Composable
fun PlaylistHeader(
    tracksCount: Int,
    onClearAllButtonClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, end = 4.dp),
    ) {
        Text(
            text = pluralStringResource(R.plurals.tracks_count, tracksCount, tracksCount),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
        )

        IconButton(onClick = onClearAllButtonClicked) {
            Icon(
                painter = painterResource(R.drawable.baseline_clear_all_24),
                contentDescription = stringResource(R.string.clear_all),
            )
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
