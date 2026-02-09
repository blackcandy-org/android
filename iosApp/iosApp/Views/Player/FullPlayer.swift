import SwiftUI
import sharedKit

struct FullPlayer: View {
    let currentSong: Song?

    var body: some View {
        VStack {
            PlayerArt(imageURL: currentSong?.albumImageUrl.large)
                .padding(.bottom, CustomStyle.spacing(.extraWide))
            PlayerInfo(currentSong: currentSong)
        }
    }
}
