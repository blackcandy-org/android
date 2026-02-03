import UIKit

func changeRootViewController(viewController: UIViewController) {
    let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate
            sceneDelegate?.window?.rootViewController = viewController
}
