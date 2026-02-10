import SwiftUI
import sharedKit

struct PlayerPlaylist: View {
    let playlist: [Song]
    let currentSong: Song?
    let onItemClicked: ((Int) -> Void)
    let onItemSweepToDismiss: ((Int) -> Void)
    let onItemMoved: ((Int, Int) -> Void)

    var body: some View {
        List {
            ForEach(playlist) { song in
                PlaylistItem(
                    song: song,
                    isCurrent: song == currentSong,
                    onClicked: { _ in }
                )
            }
            .onDelete { _ in
                // onItemSweepToDismiss()
            }
            .onMove { _, _ in
                // onItemMoved()
            }
            .listRowBackground(Color.clear)
        }
        .listStyle(.plain)
    }
}
