import Foundation
import UIKit

struct MenuItem: Identifiable {
    let id: String
    let type: ItemType
    let title: String
    let action: (() -> Void)

    init(id: String, type: ItemType = .normal, title: String, action: @escaping () -> Void) {
        self.id = id
        self.title = title
        self.action = action
        self.type = type
    }
}

extension MenuItem {
    enum ItemType {
        case normal
        case destructive
    }
}
