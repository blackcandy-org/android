import SwiftUI

struct LoginAuthenticationForm: View {
    let email: String
    let password: String
    let onLoginButtonClicked: (() -> Void)
    let onEmailChanged: ((String) -> Void)
    let onPasswordChanged: ((String) -> Void)

    var body: some View {
        Form {
            Section {
                TextField("label.email", text: Binding(
                    get: { email },
                    set: { email in onEmailChanged(email) }
                ))
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled(true)
                .keyboardType(.emailAddress)

                SecureField("label.password", text: Binding(
                    get: { password },
                    set: { password in onPasswordChanged(password) }
                ))
            }

            Button(action: {
                onLoginButtonClicked()
            }, label: {
                Text("label.login")
            })
            .frame(maxWidth: .infinity)
            .disabled(email.isEmpty || password.isEmpty)
        }
        .navigationTitle("text.login_to_bc")
        .navigationBarTitleDisplayMode(.inline)
    }
}
