package org.blackcandy.android.fragments.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dev.hotwire.turbo.fragments.TurboBottomSheetDialogFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import kotlinx.coroutines.launch
import org.blackcandy.android.R
import org.blackcandy.android.compose.account.AccountMenu
import org.blackcandy.android.databinding.FragmentSheetAccountBinding
import org.blackcandy.android.models.MenuItem
import org.blackcandy.android.models.User
import org.blackcandy.android.viewmodels.AccountSheetViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@TurboNavGraphDestination(uri = "turbo://fragment/sheets/account")
class AccountSheetFragment : TurboBottomSheetDialogFragment() {
    private val viewModel: AccountSheetViewModel by viewModel()

    private var _binding: FragmentSheetAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser: User

    private val menuItems get() =
        buildList {
            add(
                MenuItem(
                    R.string.settings,
                    R.drawable.baseline_settings_24,
                    { navigate("${viewModel.serverAddress}/setting") },
                ),
            )

            if (currentUser.isAdmin) {
                add(
                    MenuItem(
                        R.string.manage_users,
                        R.drawable.baseline_people_24,
                        { navigate("${viewModel.serverAddress}/users") },
                    ),
                )
            }

            add(
                MenuItem(
                    R.string.update_profile,
                    R.drawable.baseline_face_24,
                    { navigate("${viewModel.serverAddress}/users/${currentUser.id}/edit") },
                ),
            )

            add(
                MenuItem(
                    R.string.logout,
                    R.drawable.baseline_exit_to_app_24,
                    { viewModel.logout() },
                ),
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUserFlow.collect {
                    it?.let { currentUser = it }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSheetAccountBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            setContent {
                Mdc3Theme {
                    AccountMenu(menuItems)
                }
            }
        }

        return binding.root
    }
}
