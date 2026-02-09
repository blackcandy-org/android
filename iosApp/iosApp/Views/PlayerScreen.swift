import SwiftUI
import LNPopupUI
import sharedKit

struct PlayerScreen: View {
    private let viewModel: PlayerViewModel = KoinHelper().getPlayerViewModel()

    @State private var albumImage: UIImage?
    @State private var currentSong: Song?
    @State private var isPlaying = false

    var body: some View {
        Observing(viewModel.uiState) { uiState in
            FullPlayer(
                currentSong: uiState.musicState.currentSong
            )
        }
        .popupTitle(currentSong?.name ?? String(localized: "label.not_playing"))
        .popupImage(albumImage != nil ? Image(uiImage: albumImage!) : nil)
        .popupBarButtons {
            ToolbarItemGroup(placement: .popupBar) {
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
