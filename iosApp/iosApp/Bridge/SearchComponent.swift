import HotwireNative
import UIKit

class SearchComponent: BridgeComponent {
    private var viewController: UIViewController? {
        delegate?.destination as? UIViewController
    }

    private lazy var searchBarDelegator = SearchBarDelegator(component: self)

    override class var name: String { "search" }

    override func onReceive(message: Message) {
        switch message.event {
        case "connect":
            handleConnectEvent(message)
        default:
            break
        }
    }

    private func updateSearchResults(with query: String) {
        let data = SearchData(query: query)
        reply(to: "connect", with: data)
    }

    private func handleConnectEvent(_ message: Message) {
        guard let viewController else { return }

        let searchController = UISearchController(searchResultsController: nil)

        searchController.searchBar.delegate = searchBarDelegator

        viewController.navigationItem.searchController = searchController
        viewController.navigationItem.hidesSearchBarWhenScrolling = false
    }

}

extension SearchComponent {
    struct SearchData: Encodable {
        let query: String
    }

    private class SearchBarDelegator: NSObject, UISearchBarDelegate {
        private weak var component: SearchComponent?

        init(component: SearchComponent) {
            self.component = component
        }

        func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
            guard let query = searchBar.searchTextField.text, !query.isEmpty else { return }
            component?.updateSearchResults(with: query)
        }
    }
}
