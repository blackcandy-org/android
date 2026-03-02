import Foundation
import SwiftUI

class LoginViewController: UIHostingController<LoginScreen> {
    init() {
        super.init(rootView: LoginScreen())
    }

    @MainActor required dynamic init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
