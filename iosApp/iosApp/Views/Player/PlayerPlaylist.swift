import SwiftUI
import sharedKit

struct PlayerPlaylist: View {
    let playlist: [Song]
    let currentSong: Song?
    let onItemClicked: ((Int64) -> Void)
    let onItemSweepToDismiss: ((Int64) -> Void)
    let onItemMoved: ((Int, Int) -> Void)

    var body: some View {
        List {
            ForEach(playlist) { song in
                PlaylistItem(
                    song: song,
                    isCurrent: song == currentSong,
                    onClicked: onItemClicked
                )
            }
            .onDelete { indexSet in
                indexSet.forEach { index in
                    onItemSweepToDismiss(playlist[index].id)
                }
            }
            .onMove { fromOffsets, toOffset in
                fromOffsets.forEach { fromIndex in
                    let to = fromIndex < toOffset ? toOffset - 1 : toOffset
                    onItemMoved(fromIndex, to)
                }
            }
            .listRowBackground(Color.clear)
        }
        .listStyle(.plain)
        .toolbar {
            EditButton()
        }
    }
}
