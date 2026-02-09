import SwiftUI
import sharedKit

struct AlertMessageCover {
    static func toString(_ message: AlertMessage?) -> String {
        guard let message = message else {
            return ""
        }

        switch onEnum(of: message) {
        case .string(let string):
            return string.value ?? ""
        case .localizedString(let string):
            return getLocalizedString(definedMessage: string.value)
        }
    }

    private static func getLocalizedString(definedMessage: AlertMessage.DefinedMessages) -> String {
        switch definedMessage {
        case .unsupportedServer:
            return String(localized: "text.unsupported_server")
        case .invalidServerAddress:
            return String(localized: "text.invalid_server_address")
        case .addedToPlaylist:
            return String(localized: "text.added_to_playlist")
        }
    }
}

extension View {
    @ViewBuilder func alertMessage(_ message: AlertMessage?, onShown: @escaping () -> Void) -> some View {
        let alertMessage = AlertMessageCover.toString(message)

        alert(
            alertMessage,
            isPresented: Binding(
                get: { !alertMessage.isEmpty },
                set: { presented in
                    print(presented)
                    if !presented { onShown() }
                }
            )
        ) {}
    }

}
