import SwiftUI

struct LoginAuthenticationForm: View {
    @State var email = ""
    @State var password = ""

    var body: some View {
        Form {
            Section {
                TextField("label.email", text: $email)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled(true)
                    .keyboardType(.emailAddress)

                SecureField("label.password", text: $password)
            }

            Button(action: {
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
