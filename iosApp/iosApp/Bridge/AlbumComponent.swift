import HotwireNative
import sharedKit

class AlbumComponent: BridgeComponent {
    override class var name: String { "album" }

    private var viewModel: WebViewModel? {
        let viewController = delegate?.destination as? WebViewController
        return viewController?.viewModel
    }

    override func onReceive(message: Message) {
        switch message.event {
        case "play":
            handlePlayEvent(message)
        case "playBeginWith":
            handlePlayBeginWithEvent(message)
        default:
            break
        }
    }

    private func handlePlayEvent(_ message: Message) {
        guard let data: AlbumData = message.data() else { return }
        viewModel?.playAlbum(albumId: data.albumId)
    }

    private func handlePlayBeginWithEvent(_ message: Message) {
        guard let data: AlbumData = message.data(), let songId = data.songId else { return }
        viewModel?.playAlbumBeginWith(albumId: data.albumId, songId: songId)
    }
}

extension AlbumComponent {
    struct AlbumData: Decodable {
        let albumId: Int64
        let songId: Int64?
    }
}
