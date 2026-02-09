import SwiftUI

struct PlayerArt: View {
    let imageURL: String?

    var body: some View {
        AsyncImage(url: .init(string: imageURL ?? "")) { image in
            image.resizable()
        } placeholder: {
            Color.secondary
        }
        .cornerRadius(CustomStyle.cornerRadius(.medium))
        .frame(width: CustomStyle.playerImageSize, height: CustomStyle.playerImageSize)
    }
}
