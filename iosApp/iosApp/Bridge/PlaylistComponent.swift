import HotwireNative
import sharedKit

class PlaylistComponent: BridgeComponent {
    override class var name: String { "playlist" }

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
        guard let data: PlaylistData = message.data() else { return }
        viewModel?.playPlaylist(playlistId: data.playlistId)
    }

    private func handlePlayBeginWithEvent(_ message: Message) {
        guard let data: PlaylistData = message.data(), let songId = data.songId else { return }
        viewModel?.playPlaylistBeginWith(playlistId: data.playlistId, songId: songId)
    }
}

extension PlaylistComponent {
    struct PlaylistData: Decodable {
        let playlistId: Int64
        let songId: Int64?
    }
}
