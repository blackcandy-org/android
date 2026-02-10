import SwiftUI
import sharedKit

struct FullPlayer: View {
    let currentSong: Song?
    let currentPosition: Double
    let playbackMode: PlaybackMode
    let onPlaylistButtonClicked: (() -> Void)

    var body: some View {
        VStack {
            Spacer()

            PlayerArt(imageURL: currentSong?.albumImageUrl.large)
                .padding(.bottom, CustomStyle.spacing(.extraWide))

            PlayerInfo(currentSong: currentSong)

            PlayerControl(
                currentPosition: currentPosition,
                duration: currentSong?.duration ?? 0,
                onPreviousButtonClicked: {},
                onNextButtonClicked: {},
                onPlayButtonClicked: {},
                onPauseButtonClicked: {},
                onSeek: { _ in }
            )
            .padding(.horizontal, CustomStyle.spacing(.large))

            Spacer()

            PlayerActions(
                playbackMode: playbackMode,
                isFavorited: currentSong?.isFavorited ?? false,
                onModeSwitchButtonClicked: {},
                onFavoriteButtonClicked: {},
                onPlaylistButtonClicked: onPlaylistButtonClicked
            )
            .padding(.vertical, CustomStyle.spacing(.medium))
            .padding(.horizontal, CustomStyle.spacing(.large))
        }
    }
}
