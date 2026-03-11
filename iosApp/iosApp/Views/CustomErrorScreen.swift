import SwiftUI
import HotwireNative
import sharedKit

struct CustomErrorScreen: ErrorPresentableView {
    private let viewModel: WebViewModel = KoinHelper().getWebViewModel()

    let error: Error
    let handler: ErrorPresenter.Handler?

    var body: some View {
        VStack(spacing: CustomStyle.spacing(.medium)) {
            Image(systemName: "exclamationmark.triangle")
                .customStyle(.largeSymbol)
                .foregroundColor(.accentColor)

            Text(error.localizedDescription)
                .font(.body)
                .multilineTextAlignment(.center)

            HStack {
                if let handler {
                    Button("label.retry") {
                        handler()
                    }
                    .customStyle(.mediumFont)
                    .buttonStyle(.bordered)
                }

                Button("label.logout", role: .destructive) {
                    viewModel.logout(onSuccess: {
                        changeRootViewController(viewController: LoginViewController())
                    })
                }
                .customStyle(.mediumFont)
                .buttonStyle(.bordered)
            }
        }
        .padding(CustomStyle.spacing(.extraWide))
    }
}
