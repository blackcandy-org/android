import UIKit
import HotwireNative
import sharedKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        KoinHelperKt.doInitKoin()

        configureHotwire()

        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }

    private func configureHotwire() {
        Hotwire.config.applicationUserAgentPrefix = "\(BLACK_CANDY_USER_AGENT);"

        Hotwire.loadPathConfiguration(from: [
            .file(Bundle.main.url(forResource: "configuration", withExtension: "json")!)
        ])

        Hotwire.config.defaultViewController = { url in
            WebViewController(url: url)
        }

        Hotwire.registerBridgeComponents([
            AccountComponent.self,
            SearchComponent.self
        ])
    }
}
