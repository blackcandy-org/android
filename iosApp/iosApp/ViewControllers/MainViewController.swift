import UIKit
import HotwireNative

class MainViewController: UISplitViewController, UISplitViewControllerDelegate {
    init(serverAddress: String) {
        super.init(style: .doubleColumn)

        preferredDisplayMode = .oneBesideSecondary
        preferredSplitBehavior = .tile
        presentsWithGesture = false
        delegate = self

        let tabBarController = HotwireTabBarController(navigatorDelegate: self)
        let tabs = buildMainTabs(serverAddress: serverAddress)

        tabBarController.load(tabs)

        setViewController(tabBarController, for: .secondary)
        setViewController(tabBarController, for: .compact)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

extension MainViewController: NavigatorDelegate {
    func handle(proposal: HotwireNative.VisitProposal, from navigator: HotwireNative.Navigator) -> HotwireNative.ProposalResult {
        return .accept
    }
}
