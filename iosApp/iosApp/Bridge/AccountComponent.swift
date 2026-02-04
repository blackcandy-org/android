import Foundation
import SwiftUI
import UIKit
import HotwireNative
import sharedKit

class AccountComponent: BridgeComponent {
    private  var menuItems: [MenuItem] = []

    private var viewController: WebViewController? {
        delegate?.destination as? WebViewController
    }

    private var viewModel: WebViewModel? {
        viewController?.viewModel
    }

    override class var name: String { "account" }

    override func onReceive(message: Message) {
        switch message.event {
        case "connect":
            handleConnectEvent()
        case "menuItemConnected:settings":
            handleMenuItemConnectedEvent("settings")
        case "menuItemConnected:manage_users":
            handleMenuItemConnectedEvent("manage_users")
        case "menuItemConnected:update_profile":
            handleMenuItemConnectedEvent("update_profile")
        case "menuItemConnected:logout":
            handleMenuItemConnectedEvent("logout")
        default:
            break
        }

    }

    private func handleConnectEvent() {
        guard let viewController else { return }

        let action = UIAction { [unowned self] _ in
            viewController.present(
                UIHostingController(
                    rootView: AccountMenu(menuItems: menuItems)
                ),
                animated: true
            )
        }

        let item = UIBarButtonItem(title: String(localized: "label.account"), primaryAction: action)
        item.image = .init(systemName: "person.circle")

        viewController.navigationItem.rightBarButtonItem = item
    }

    private func handleMenuItemConnectedEvent(_ id: String) {
        if menuItems.contains(where: { $0.id == id }) { return }

        switch id {
        case "settings":
            menuItems.append(
                .init(
                    id: "settings",
                    title: String(localized: "label.settings"),
                    action: {
                        self.reply(to: "menuItemConnected:settings")
                    }
                )
            )
        case "manage_users":
            menuItems.append(
                .init(
                    id: "manage_users",
                    title: String(localized: "label.manage_users"),
                    action: {
                        self.reply(to: "menuItemConnected:manage_users")
                    }
                )
            )
        case "update_profile":
            menuItems.append(
                .init(
                    id: "update_profile",
                    title: String(localized: "label.update_profile"),
                    action: {
                        self.reply(to: "menuItemConnected:update_profile")
                    }
                )
            )
        case "logout":
            menuItems.append(
                .init(
                    id: "logout",
                    type: .destructive,
                    title: String(localized: "label.logout"),
                    action: {
                        self.viewModel?.logout(onSuccess: {
                            changeRootViewController(viewController: LoginViewController())
                        })
                    }
                )
            )
        default:
            break
        }
    }
}
