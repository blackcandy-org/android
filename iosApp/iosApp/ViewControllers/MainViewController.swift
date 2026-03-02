import UIKit
import HotwireNative
import LNPopupUI
import sharedKit

class MainViewController: UISplitViewController, UISplitViewControllerDelegate {
    private let musicServiceViewModel: MusicServiceViewModel = KoinHelper().getMusicServiceViewModel()

    init(serverAddress: String) {
        super.init(style: .doubleColumn)

        preferredDisplayMode = .oneBesideSecondary
        preferredSplitBehavior = .tile
        presentsWithGesture = false
        delegate = self

        let tabBarController = HotwireTabBarController(navigatorDelegate: self)
        let tabs = buildMainTabs(serverAddress: serverAddress)

        tabBarController.load(tabs)
        tabBarController.presentPopupBar {
            PlayerScreen()
        }

        setViewController(tabBarController, for: .secondary)
        setViewController(tabBarController, for: .compact)

        musicServiceViewModel.setupMusicServiceController()
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
