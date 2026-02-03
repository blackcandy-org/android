import Foundation
import HotwireNative

func buildMainTabs(serverAddress: String) -> [HotwireTab] {
    [
        .init(title: String(localized: "label.home"), image: .init(systemName: "house")!, url: URL(string: serverAddress)!),
        .init(title: String(localized: "label.library"), image: .init(systemName: "square.stack")!, url: URL(string: "\(serverAddress)/library")!)
    ]
}
