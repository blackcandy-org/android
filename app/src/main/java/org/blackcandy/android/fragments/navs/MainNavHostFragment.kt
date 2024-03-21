package org.blackcandy.android.fragments.navs

import android.os.Bundle
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlinx.coroutines.launch
import org.blackcandy.android.fragments.sheets.AccountSheetFragment
import org.blackcandy.android.fragments.web.WebBottomSheetFragment
import org.blackcandy.android.fragments.web.WebFragment
import org.blackcandy.android.fragments.web.WebHomeFragment
import org.blackcandy.android.fragments.web.WebLibraryFragment
import org.blackcandy.android.utils.BLACK_CANDY_USER_AGENT
import org.blackcandy.android.utils.PlayableResource
import org.blackcandy.android.utils.SnackbarUtil.Companion.showSnackbar
import org.blackcandy.android.utils.Theme
import org.blackcandy.android.viewmodels.NavHostViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

open class MainNavHostFragment : TurboSessionNavHostFragment() {
    private val viewModel: NavHostViewModel by viewModel()

    override val sessionName = "main"
    override val startLocation get() = viewModel.serverAddress

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() =
            listOf(
                // Leave empty unless you have more
                // than one TurboActivity in your app
            )

    override val registeredFragments: List<KClass<out Fragment>>
        get() =
            listOf(
                WebFragment::class,
                WebHomeFragment::class,
                WebLibraryFragment::class,
                WebBottomSheetFragment::class,
                AccountSheetFragment::class,
                // And any other TurboFragments in your app
            )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() =
            TurboPathConfiguration.Location(
                assetFilePath = "json/configuration.json",
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.alertMessage != null) {
                        showSnackbar(requireActivity(), it.alertMessage) {
                            viewModel.alertMessageShown()
                        }
                    }
                }
            }
        }
    }

    override fun onSessionCreated() {
        super.onSessionCreated()

        session.webView.settings.userAgentString = BLACK_CANDY_USER_AGENT

        session.webView.addJavascriptInterface(
            object {
                @JavascriptInterface
                fun updateTheme(theme: String) {
                    val themeValue = Theme.values().find { it.name == theme.uppercase() } ?: return
                    viewModel.updateTheme(themeValue)
                }

                @JavascriptInterface
                fun playAll(
                    resourceType: String,
                    resourceId: Int,
                ) {
                    val resourceTypeValue = PlayableResource.values().find { it.name == resourceType.uppercase() } ?: return
                    viewModel.playAll(resourceTypeValue, resourceId)
                }

                @JavascriptInterface
                fun playSong(songId: Int) {
                    viewModel.playSong(songId)
                }

                @JavascriptInterface
                fun playNext(songId: Int) {
                    viewModel.playNext(songId)
                }

                @JavascriptInterface
                fun playLast(songId: Int) {
                    viewModel.playLast(songId)
                }

                @JavascriptInterface
                fun showFlashMessage(message: String) {
                    viewModel.showFlashMessage(message)
                }
            },
            "NativeBridge",
        )
    }
}
