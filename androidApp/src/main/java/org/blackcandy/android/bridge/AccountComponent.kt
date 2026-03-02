package org.blackcandy.android.bridge

import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import org.blackcandy.android.R
import org.blackcandy.android.compose.account.AccountMenu
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.android.utils.MenuItem
import org.blackcandy.shared.viewmodels.WebViewModel

class AccountComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>,
) : BridgeComponent<HotwireDestination>(name, delegate) {
    private val fragment: WebFragment
        get() = delegate.destination.fragment as WebFragment

    private val toolbar: MaterialToolbar?
        get() = fragment.view?.findViewById(R.id.toolbar)

    private val menuItems: MutableList<MenuItem> = mutableListOf()

    private val viewModel: WebViewModel
        get() = fragment.viewModel

    private lateinit var bottomSheet: BottomSheetDialog

    override fun onReceive(message: Message) {
        when (message.event) {
            "connect" -> handleConnectEvent(message)
            "menuItemConnected:settings" -> handleMenuItemConnectedEvent("settings")
            "menuItemConnected:manage_users" -> handleMenuItemConnectedEvent("manage_users")
            "menuItemConnected:update_profile" -> handleMenuItemConnectedEvent("update_profile")
            "menuItemConnected:logout" -> handleMenuItemConnectedEvent("logout")
        }
    }

    private fun handleConnectEvent(message: Message) {
        val view = fragment.view?.rootView ?: return
        bottomSheet = BottomSheetDialog(view.context)

        val composeView =
            ComposeView(view.context).apply {
                setContent {
                    Mdc3Theme {
                        AccountMenu(menuItems)
                    }
                }
            }

        bottomSheet.setContentView(composeView)

        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.top_bar_account -> {
                    bottomSheet.show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun handleMenuItemConnectedEvent(id: String) {
        if (menuItems.any { it.id == id }) {
            return
        }

        when (id) {
            "settings" -> {
                menuItems.add(
                    MenuItem("settings", R.string.settings, R.drawable.baseline_settings_24, {
                        replyTo("menuItemConnected:settings")
                        bottomSheet.dismiss()
                    }),
                )
            }

            "manage_users" -> {
                menuItems.add(
                    MenuItem("manage_users", R.string.manage_users, R.drawable.baseline_people_24, {
                        replyTo("menuItemConnected:manage_users")
                        bottomSheet.dismiss()
                    }),
                )
            }

            "update_profile" -> {
                menuItems.add(
                    MenuItem("update_profile", R.string.update_profile, R.drawable.baseline_face_24, {
                        replyTo("menuItemConnected:update_profile")
                        bottomSheet.dismiss()
                    }),
                )
            }

            "logout" -> {
                menuItems.add(
                    MenuItem("logout", R.string.logout, R.drawable.baseline_exit_to_app_24, {
                        viewModel.logout()
                    }),
                )
            }
        }
    }
}
