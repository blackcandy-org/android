import Foundation
import HotwireNative
import sharedKit

class WebViewController: HotwireWebViewController {
    let viewModel: WebViewModel = KoinHelper().getWebViewModel()
}
