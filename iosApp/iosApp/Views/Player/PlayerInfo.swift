import SwiftUI
import sharedKit

struct PlayerInfo: View {
    let currentSong: Song?

    var body: some View {
        VStack(spacing: CustomStyle.spacing(.tiny)) {
            Text(currentSong?.name ?? String(localized: "label.not_playing"))
                .font(.headline)
            Text(currentSong?.artistName ?? "")
                .font(.caption)
        }
    }
}
