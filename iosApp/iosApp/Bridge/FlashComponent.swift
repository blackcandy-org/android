import HotwireNative
import sharedKit

class FlashComponent: BridgeComponent {
    override class var name: String { "flash" }

    private var viewModel: WebViewModel? {
        let viewController = delegate?.destination as? WebViewController
        return viewController?.viewModel
    }

    override func onReceive(message: Message) {
        switch message.event {
        case "connect":
            handleConnectEvent(message)
        default:
            break
        }
    }

    private func handleConnectEvent(_ message: Message) {
        guard let data: MessageData = message.data() else { return }
        viewModel?.showFlashMessage(message: data.message)
    }
}

extension FlashComponent {
    struct MessageData: Decodable {
        let message: String
    }
}
