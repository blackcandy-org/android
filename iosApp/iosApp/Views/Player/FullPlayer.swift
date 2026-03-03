import SwiftUI
import sharedKit

struct FullPlayer: View {
    let isCompactMode: Bool
    let currentSong: Song?
    let currentPosition: Double
    let playbackMode: PlaybackMode
    let isPlaying: Bool
    let isLoading: Bool
    let onPreviousButtonClicked: (() -> Void)
    let onNextButtonClicked: (() -> Void)
    let onPlayButtonClicked: (() -> Void)
    let onPauseButtonClicked: (() -> Void)
    let onPlaylistButtonClicked: (() -> Void)?
    let onModeSwitchButtonClicked: (() -> Void)
    let onFavoriteButtonClicked: (() -> Void)
    let onSeek: ((Double) -> Void)

    var body: some View {
        VStack {
            if isCompactMode {
                Spacer()
            }

            PlayerArt(imageURL: currentSong?.albumImageUrl.large)
                .padding(.bottom, CustomStyle.spacing(.extraWide))

            PlayerInfo(currentSong: currentSong)

            PlayerControl(
                isPlaying: isPlaying,
                isLoading: isLoading,
                currentPosition: currentPosition,
                duration: currentSong?.duration ?? 0,
                onPreviousButtonClicked: onPreviousButtonClicked,
                onNextButtonClicked: onNextButtonClicked,
                onPlayButtonClicked: onPlayButtonClicked,
                onPauseButtonClicked: onPauseButtonClicked,
                onSeek: onSeek
            )
            .padding(.horizontal, CustomStyle.spacing(.large))

            if isCompactMode {
                Spacer()
            }

            PlayerActions(
                playbackMode: playbackMode,
                isFavorited: currentSong?.isFavorited ?? false,
                onModeSwitchButtonClicked: onModeSwitchButtonClicked,
                onFavoriteButtonClicked: onFavoriteButtonClicked,
                onPlaylistButtonClicked: onPlaylistButtonClicked
            )
            .padding(.vertical, CustomStyle.spacing(.medium))
            .padding(.horizontal, CustomStyle.spacing(.large))
        }
    }
}
