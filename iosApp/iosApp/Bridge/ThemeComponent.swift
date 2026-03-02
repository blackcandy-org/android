import HotwireNative
import sharedKit

class ThemeComponent: BridgeComponent {
    override class var name: String { "theme" }

    private var viewModel: WebViewModel? {
        let viewController = delegate?.destination as? WebViewController
        return viewController?.viewModel
    }

    override func onReceive(message: Message) {
        switch message.event {
        case "initialize":
            handleInitializeEvent(message)
        default:
            break
        }
    }

    private func handleInitializeEvent(_ message: Message) {
        guard let data: ThemeData = message.data() else { return }
        guard let themeValue = Theme.allCases.first(where: { $0.name == data.theme.uppercased() }) else { return }

        viewModel?.updateTheme(theme: themeValue)
    }
}

extension ThemeComponent {
    struct ThemeData: Decodable {
        let theme: String
    }
}
