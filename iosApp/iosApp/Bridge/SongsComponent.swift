import HotwireNative
import sharedKit

class SongsComponent: BridgeComponent {
    override class var name: String { "songs" }

    private var viewModel: WebViewModel? {
        let viewController = delegate?.destination as? WebViewController
        return viewController?.viewModel
    }

    override func onReceive(message: Message) {
        switch message.event {
        case "playNow":
            handlePlayNowEvent(message)
        case "playNext":
            handlePlayNextEvent(message)
        case "playLast":
            handlePlayLastEvent(message)
        default:
            break
        }
    }

    private func handlePlayNowEvent(_ message: Message) {
        guard let data: SongsData = message.data() else { return }
        viewModel?.playNow(songId: data.songId)
    }

    private func handlePlayNextEvent(_ message: Message) {
        guard let data: SongsData = message.data() else { return }
        viewModel?.playNext(songId: data.songId)
    }

    private func handlePlayLastEvent(_ message: Message) {
        guard let data: SongsData = message.data() else { return }
        viewModel?.playLast(songId: data.songId)
    }
}

extension SongsComponent {
    struct SongsData: Decodable {
        let songId: Int64
    }
}
