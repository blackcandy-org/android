import Foundation
import HotwireNative
import sharedKit
import AlertKit

class WebViewController: HotwireWebViewController {
    let viewModel: WebViewModel = KoinHelper().getWebViewModel()

    private var observationTask: Task<Void, Never>?

    override func viewDidLoad() {
        super.viewDidLoad()

        observationTask = Task { [weak self] in
            guard let viewModel = self?.viewModel else { return }

            for await uiState in viewModel.uiState {
                guard let alertMessage = uiState.alertMessage else { continue }

                AlertKitAPI.present(
                    title: AlertMessageCover.toString(alertMessage),
                    style: .iOS17AppleMusic,
                    haptic: .success
                )

                viewModel.alertMessageShown()
            }
        }
    }

    deinit {
        observationTask?.cancel()
    }
}
