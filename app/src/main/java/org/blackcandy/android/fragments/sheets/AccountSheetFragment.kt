package org.blackcandy.android.fragments.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dev.hotwire.turbo.fragments.TurboBottomSheetDialogFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import org.blackcandy.android.R
import org.blackcandy.android.databinding.FragmentSheetAccountBinding
import org.blackcandy.android.models.MenuItem

@TurboNavGraphDestination(uri = "turbo://fragment/sheets/account")
class AccountSheetFragment : TurboBottomSheetDialogFragment() {
    private var _binding: FragmentSheetAccountBinding? = null
    private val binding get() = _binding!!

    private val menuItems get() = listOf(
        MenuItem(R.string.settings, R.drawable.baseline_settings_24),
        MenuItem(R.string.manage_users, R.drawable.baseline_people_24),
        MenuItem(R.string.update_profile, R.drawable.baseline_face_24),
        MenuItem(R.string.logout, R.drawable.baseline_exit_to_app_24),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

@Composable
fun AccountMenu(menuItems: List<MenuItem>) {
    Column {
        menuItems.forEach {
            AccountMenuItem(
                title = stringResource(id = it.titleResourceId),
                iconResourceId = it.iconResourceId,
            )
        }
    }
}

@Composable
fun AccountMenuItem(title: String, iconResourceId: Int) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = { Text(title) },
        leadingContent = {
            Icon(
                painterResource(id = iconResourceId),
                contentDescription = title,
            )
        },
    )
}
