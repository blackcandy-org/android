import SwiftUI
import sharedKit

struct LoginScreen: View {
    @State var isAuthenticationFormVisible: Bool = false
    @State var serverAddress: String?
    @State var email: String = ""
    @State var password: String = ""
    @State var showingAlert = false
    @State var alertMessage: AlertMessage?

    private let viewModel: LoginViewModel = KoinHelper().getLoginViewModel()

    var body: some View {
        NavigationView {
            VStack {
                LoginConnectionForm(
                    serverAddress: serverAddress ?? "",
                    onConnectButtonClicked: {
                        viewModel.checkSystemInfo(onSuccess: { isAuthenticationFormVisible = true })
                    },
                    onServerAddressChanged: { serverAddress in
                        viewModel.updateServerAddress(serverAddress: serverAddress)
                    }
                )

                NavigationLink(
                    destination: LoginAuthenticationForm(
                        email: email,
                        password: password,
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
        .alertMessage(alertMessage, isPresented: $showingAlert, onShown: {
            viewModel.alertMessageShown()
        })
        .collect(flow: viewModel.uiState) { state in
            serverAddress = state.serverAddress
            email = state.email
            password = state.password

            if let message = state.alertMessage {
                alertMessage = message
                showingAlert = true
            }
        }
    }
}
