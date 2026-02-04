import SwiftUI

struct AccountMenu: View {
    let menuItems: [MenuItem]

    var body: some View {
        List(menuItems) { item in
            if item.type == .destructive {
                Section {
                    Button(
                        role: .destructive,
                        action: {
                            item.action()
                        },
                        label: {
                            Text(item.title)
                        }
                    )
                    .frame(maxWidth: .infinity)
                }
            } else {
                Button(item.title) {
                    item.action()
                }
            }
        }
        .listStyle(.insetGrouped)
    }
}
