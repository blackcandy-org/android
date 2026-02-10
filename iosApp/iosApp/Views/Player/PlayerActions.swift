import SwiftUI
import sharedKit

struct PlayerActions: View {
    let playbackMode: PlaybackMode
    let isFavorited: Bool
    let onModeSwitchButtonClicked: (() -> Void)
    let onFavoriteButtonClicked: (() -> Void)
    let onPlaylistButtonClicked: (() -> Void)

    var body: some View {
        HStack {
            Button(
                action: {
                    onModeSwitchButtonClicked()
                },
                label: {
                    playbackModeIcon(playbackMode)
                }
            )
            .padding(CustomStyle.spacing(.narrow))
            .background(playbackMode == .noRepeat ? .clear : .accentColor)
            .cornerRadius(CustomStyle.cornerRadius(.medium))

            Spacer()

            Button(
                action: {
                    onFavoriteButtonClicked()
                },
                label: {
                    if isFavorited {
                        Image(systemName: "heart.fill")
                            .tint(.red)
                    } else {
                        Image(systemName: "heart")
                            .tint(.primary)
                    }
                }
            )
            .padding(CustomStyle.spacing(.narrow))

            Spacer()

            Button(
                action: {
                    onPlaylistButtonClicked()
                },
                label: {
                    Image(systemName: "list.bullet")
                        // .tint(viewStore.isPlaylistVisible ? .white : .primary)
                }
            )
            .padding(CustomStyle.spacing(.narrow))
            // .background(viewStore.isPlaylistVisible ? Color.accentColor : .clear)
            .cornerRadius(CustomStyle.cornerRadius(.medium))
        }
    }

    func playbackModeIcon(_ mode: PlaybackMode) -> some View {
        let iconName = switch mode {
        case .noRepeat:
            "repeat"
        case .repeat:
            "repeat"
        case .repeatOne:
            "repeat.1"
        case .shuffle:
            "shuffle"
        }

        return Image(systemName: iconName)
    }
}
