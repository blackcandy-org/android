import SwiftUI
import sharedKit

struct LoginScreen: View {
    private let viewModel: LoginViewModel = KoinHelper().getLoginViewModel()

    @State private var path = NavigationPath()

    var body: some View {
        Observing(viewModel.uiState) { uiState in
            NavigationStack(path: $path) {
                LoginConnectionForm(
                    serverAddress: uiState.serverAddress ?? "",
                    onConnectButtonClicked: {
                        viewModel.checkSystemInfo(onSuccess: { path.append(Route.authentication) })
                    },
                    onServerAddressChanged: { serverAddress in
                        viewModel.updateServerAddress(serverAddress: serverAddress)
                    }
                )
                .frame(maxWidth: CustomStyle.loginFormMaxWidth)
                .navigationDestination(for: Route.self) { route in
                    switch route {
                    case .authentication:
                        LoginAuthenticationForm(
                            email: uiState.email,
                            password: uiState.password,
                            onLoginButtonClicked: {
                                viewModel.login(onSuccess: { serverAddress in
                                    changeRootViewController(viewController: MainViewController(serverAddress: serverAddress))
                                })
                            },
                            onEmailChanged: { email in
                                viewModel.updateEmail(email: email)
                            },
                            onPasswordChanged: { password in
                                viewModel.updatePassword(password: password)
                            }
                        )
                        .frame(maxWidth: CustomStyle.loginFormMaxWidth)
                    }
                }
            }
            .navigationViewStyle(.stack)
            .alertMessage(uiState.alertMessage, onShown: {
                viewModel.alertMessageShown()
            })
        }
    }
}

extension LoginScreen {
    enum Route: Hashable {
        case authentication
    }
}
