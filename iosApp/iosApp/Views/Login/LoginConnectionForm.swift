import SwiftUI

struct LoginConnectionForm: View {
    let serverAddress: String
    let onConnectButtonClicked: (() -> Void)
    let onServerAddressChanged: ((String) -> Void)

    var body: some View {
        Form {
            Section(content: {
                TextField("label.server_address", text: Binding(
                    get: { serverAddress },
                    set: { serverAddress in onServerAddressChanged(serverAddress) }
                ))
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled(true)
                .keyboardType(.URL)
            }, header: {
                Image("BlackCandyLogo")
                    .frame(maxWidth: .infinity)
                    .padding(.bottom)
            })

            Button(action: {
                onConnectButtonClicked()
            }, label: {
                Text("label.connect")
            })
            .frame(maxWidth: .infinity)
            .disabled(serverAddress.isEmpty)
        }
        .navigationTitle("text.connect_to_bc")
        .navigationBarTitleDisplayMode(.inline)
    }
}
