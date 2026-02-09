import SwiftUI

struct PlayerControl: View {
    let isPlaying = false
    let isLoading = false
    let currentPosition: Double
    let duration: Double
    let enabled = true
    let onPreviousButtonClicked: (() -> Void)
    let onNextButtonClicked: (() -> Void)
    let onPlayButtonClicked: (() -> Void)
    let onPauseButtonClicked: (() -> Void)
    let onSeek: ((Double) -> Void)

    var body: some View {
        VStack {

        }
    }
}
