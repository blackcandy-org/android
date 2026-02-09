import SwiftUI
import sharedKit

struct LoginScreen: View {
    @State var isAuthenticationFormVisible: Bool = false

    private let viewModel: LoginViewModel = KoinHelper().getLoginViewModel()

    var body: some View {
        Observing(viewModel.uiState) { uiState in
            NavigationView {
                VStack {
                    LoginConnectionForm(
                        serverAddress: uiState.serverAddress ?? "",
                        onConnectButtonClicked: {
                            viewModel.checkSystemInfo(onSuccess: { isAuthenticationFormVisible = true })
                        },
                        onServerAddressChanged: { serverAddress in
                            viewModel.updateServerAddress(serverAddress: serverAddress)
                        }
                    )

                    NavigationLink(
                        destination: LoginAuthenticationForm(
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
                        ),
                        isActive: $isAuthenticationFormVisible,
                        label: { EmptyView() }
                    )
                    .hidden()
                }
            }
            .navigationViewStyle(.stack)
            .alertMessage(uiState.alertMessage, onShown: {
                viewModel.alertMessageShown()
            })
        }
    }
}
