import SwiftUI
import sharedKit

struct LoginScreen: View {
    @State var isAuthenticationFormVisible: Bool = false
    @State var serverAddress: String?
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
                    destination: LoginAuthenticationForm(),
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

            if let message = state.alertMessage {
                alertMessage = message
                showingAlert = true
            }
        }
    }
}
