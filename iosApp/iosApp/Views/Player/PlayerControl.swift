import SwiftUI
import sharedKit

struct PlayerControl: View {
    let isPlaying: Bool
    let isLoading: Bool
    let currentPosition: Double
    let duration: Double
    let enabled = true
    let onPreviousButtonClicked: (() -> Void)
    let onNextButtonClicked: (() -> Void)
    let onPlayButtonClicked: (() -> Void)
    let onPauseButtonClicked: (() -> Void)
    let onSeek: ((Double) -> Void)

    var body: some View {
        let progressValue = duration > 0 ? (currentPosition / duration) : 0
        let currentPositionText = enabled ? DurationFormatter.companion.string(duration: currentPosition) : NONE_DURATION_TEXT
        let durationText = enabled ? DurationFormatter.companion.string(duration: duration) : NONE_DURATION_TEXT

        VStack {
            PlayerSlider(value: Binding(
                get: { progressValue },
                set: { onSeek($0) }))

            HStack {
                if isLoading {
                    ProgressView()
                        .customStyle(.playerProgressLoader)
                } else {
                    Text(currentPositionText)
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }

                Spacer()

                Text(durationText)
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
        }

        HStack {
            Button(
                action: {
                    onPreviousButtonClicked()
                },
                label: {
                    Image(systemName: "backward.fill")
                        .tint(.primary)
                        .customStyle(.largeSymbol)
                }
            )

            Spacer()

            Button(
                action: {
                    if isPlaying {
                        onPauseButtonClicked()
                    } else {
                        onPlayButtonClicked()
                    }
                },
                label: {
                    if isPlaying {
                        Image(systemName: "pause.fill")
                            .tint(.primary)
                            .customStyle(.extraLargeSymbol)
                    } else {
                        Image(systemName: "play.fill")
                            .tint(.primary)
                            .customStyle(.extraLargeSymbol)
                    }
                }
            )

            Spacer()

            Button(
                action: {
                    onNextButtonClicked()
                },
                label: {
                    Image(systemName: "forward.fill")
                        .tint(.primary)
                        .customStyle(.largeSymbol)
                }
            )

        }
        .padding(CustomStyle.spacing(.large))
    }
}
