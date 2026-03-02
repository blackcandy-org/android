import SwiftUI
import sharedKit

struct PlaylistItem: View {
    let song: Song
    let isCurrent: Bool
    let onClicked: ((Int64) -> Void)

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: CustomStyle.spacing(.small)) {
                Text(song.name)
                    .customStyle(.mediumFont)

                Text(song.artistName)
                    .customStyle(.smallFont)
            }

            Spacer()

            Text(DurationFormatter.companion.string(duration: song.duration))
                .customStyle(.smallFont)
        }
        .foregroundColor(isCurrent ? .accentColor : .primary)
        .onTapGesture {
            onClicked(song.id)
        }
    }
}
