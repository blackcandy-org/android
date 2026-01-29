import Foundation
import SwiftUI

class LoginViewController: UIHostingController<LoginView> {
    init() {
        super.init(rootView: LoginView())
    }

    @MainActor required dynamic init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
