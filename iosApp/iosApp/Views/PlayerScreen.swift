import SwiftUI
import LNPopupUI
import sharedKit

struct PlayerScreen: View {
    private let viewModel: PlayerViewModel = KoinHelper().getPlayerViewModel()

    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @State private var path = NavigationPath()
    @State private var albumImage: UIImage?
    @State private var currentSong: Song?
    @State private var isPlaying = false

    var body: some View {
        Observing(viewModel.uiState) { uiState in
            if horizontalSizeClass == .regular {
                HStack(spacing: CustomStyle.spacing(.ultraWide)) {
                    FullPlayer(
                        isCompactMode: false,
                        currentSong: uiState.musicState.currentSong,
                        currentPosition: uiState.currentPosition,
                        playbackMode: uiState.musicState.playbackMode,
                        isPlaying: uiState.musicState.isPlaying,
                        isLoading: uiState.musicState.isLoading,
                        onPreviousButtonClicked: { viewModel.previous() },
                        onNextButtonClicked: { viewModel.next() },
                        onPlayButtonClicked: { viewModel.play() },
                        onPauseButtonClicked: { viewModel.pause() },
                        onPlaylistButtonClicked: nil,
                        onModeSwitchButtonClicked: { viewModel.nextMode() },
                        onFavoriteButtonClicked: { viewModel.toggleFavorite() },
                        onSeek: { viewModel.seekToRatio(ratio: $0) }
                    )

                    VStack {
                        HStack {
                            Text("label.tracks(\(uiState.musicState.playlist.count))")
                            Spacer()
                            EditButton()
                        }
                        .padding(CustomStyle.spacing(.medium))
                        .cornerRadius(CustomStyle.cornerRadius(.large))

                        PlayerPlaylist(
                            playlist: uiState.musicState.playlist,
                            currentSong: uiState.musicState.currentSong,
                            onItemClicked: { viewModel.playOn(songId: $0) },
                            onItemSweepToDismiss: { viewModel.removeSongFromPlaylist(songId: $0) },
                            onItemMoved: { from, to in viewModel.moveSongInPlaylist(from: Int32(from), to: Int32(to)) }
                        )

                    }

                    .frame(maxHeight: CustomStyle.playlistMaxHeight)
                }
            } else {
                NavigationStack(path: $path) {
                    FullPlayer(
                        isCompactMode: true,
                        currentSong: uiState.musicState.currentSong,
                        currentPosition: uiState.currentPosition,
                        playbackMode: uiState.musicState.playbackMode,
                        isPlaying: uiState.musicState.isPlaying,
                        isLoading: uiState.musicState.isLoading,
                        onPreviousButtonClicked: { viewModel.previous() },
                        onNextButtonClicked: { viewModel.next() },
                        onPlayButtonClicked: { viewModel.play() },
                        onPauseButtonClicked: { viewModel.pause() },
                        onPlaylistButtonClicked: { path.append(Route.playlist) },
                        onModeSwitchButtonClicked: { viewModel.nextMode() },
                        onFavoriteButtonClicked: { viewModel.toggleFavorite() },
                        onSeek: { viewModel.seekToRatio(ratio: $0) }
                    )
                    .navigationDestination(for: Route.self) { route in
                        switch route {
                        case .playlist:
                            PlayerPlaylist(
                                playlist: uiState.musicState.playlist,
                                currentSong: uiState.musicState.currentSong,
                                onItemClicked: { viewModel.playOn(songId: $0) },
                                onItemSweepToDismiss: { viewModel.removeSongFromPlaylist(songId: $0) },
                                onItemMoved: { from, to in viewModel.moveSongInPlaylist(from: Int32(from), to: Int32(to)) }
                            )
                        }
                    }
                }
            }
        }
        .popupTitle(currentSong?.name ?? String(localized: "label.not_playing"))
        .popupImage(albumImage != nil ? Image(uiImage: albumImage!) : nil)
        .popupBarButtons {
            ToolbarItemGroup(placement: .popupBar) {
                if horizontalSizeClass == .regular {
                    Button(
                        action: {
                            viewModel.previous()
                        },
                        label: {
                            Image(systemName: "backward.fill")
                                .tint(.primary)
                        }
                    )
                }
                Button(
                    action: {
                        if isPlaying {
                            viewModel.pause()
                        } else {
                            viewModel.play()
                        }
                    },
                    label: {
                        if isPlaying {
                            Image(systemName: "pause.fill")
                                .tint(.primary)
                        } else {
                            Image(systemName: "play.fill")
                                .tint(.primary)
                        }
                    }
                )

                Button(
                    action: {
                        viewModel.next()
                    },
                    label: {
                        Image(systemName: "forward.fill")
                            .tint(.primary)
                    }
                )
            }
        }
        .task(id: currentSong?.id) {
            guard let urlString = currentSong?.albumImageUrl.small,
                  let url = URL(string: urlString) else {
                albumImage = nil
                return
            }

            do {
                let (data, _) = try await URLSession.shared.data(from: url)
                if let image = UIImage(data: data) {
                    albumImage = image
                }
            } catch {
                albumImage = nil
            }
        }
        .collect(flow: viewModel.uiState) { state in
            currentSong = state.musicState.currentSong
            isPlaying = state.musicState.isPlaying
        }
    }
}

extension PlayerScreen {
    enum Route: Hashable {
        case playlist
    }
}
