package org.blackcandy.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationBarView
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.tabs.HotwireNavigationController
import dev.hotwire.navigation.tabs.HotwireTab
import dev.hotwire.navigation.tabs.navigatorConfigurations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.compose.player.MiniPlayer
import org.blackcandy.android.compose.player.PlayerScreen
import org.blackcandy.android.databinding.ActivityMainBinding
import org.blackcandy.shared.viewmodels.MainViewModel
import org.blackcandy.shared.viewmodels.MusicServiceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : HotwireActivity() {
    private lateinit var navigationController: HotwireNavigationController

    private val viewModel: MainViewModel by viewModel()

    private val musicServiceViewModel: MusicServiceViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerBottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var mainTabs: List<HotwireTab>

    private val playerBottomSheetCallback by lazy {
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float,
            ) {
                setupSlideTransition(slideOffset)
            }

            override fun onStateChanged(
                bottomSheet: View,
                newState: Int,
            ) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requireLogin()) {
            switchToLoginActivity()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        musicServiceViewModel.setupMusicServiceController()

        setContentView(binding.root)

        setupLayout()
        setupBottomTabs()
        setupPlayerBottomSheet()
        setupMiniPlayer()
        setupPlayerScreen()
    }

    override fun onRestart() {
        super.onRestart()

        musicServiceViewModel.getCurrentPlaylist()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::playerBottomSheetBehavior.isInitialized) {
            playerBottomSheetBehavior.removeBottomSheetCallback(playerBottomSheetCallback)
        }
    }

    override fun onResume() {
        super.onResume()

        if (::playerBottomSheetBehavior.isInitialized &&
            playerBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
        ) {
            setupSlideTransition(1f)
        }
    }

    override fun navigatorConfigurations(): List<NavigatorConfiguration> {
        mainTabs = buildMainTabs(viewModel.serverAddress)
        return mainTabs.navigatorConfigurations
    }

    private fun requireLogin(): Boolean {
        viewModel.currentUser ?: return true

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUserFlow.collect {
                    if (it == null) {
                        switchToLoginActivity()
                    }
                }
            }
        }

        return false
    }

    private fun setupLayout() {
        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val displayCutout = windowInsets.displayCutout

            // Because displaying edge-to-edge, so the height of bottom nav includes the height of system navigation bar.
            val bottomNavHeightWithNav = binding.bottomNav?.height ?: 0
            val systemNavigationBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            val miniPlayerHeight =
                if (binding.miniPlayerComposeView != null) {
                    resources.getDimensionPixelSize(
                        R.dimen.mini_player_height,
                    )
                } else {
                    0
                }

            val containerMarginSize =
                when (true) {
                    (binding.bottomNav != null && binding.miniPlayerComposeView != null) -> bottomNavHeightWithNav + miniPlayerHeight
                    (binding.bottomNav == null && binding.miniPlayerComposeView != null) -> systemNavigationBarHeight + miniPlayerHeight
                    (binding.bottomNav == null && binding.miniPlayerComposeView == null) -> systemNavigationBarHeight
                    else -> 0
                }

            val homeContainerLayoutParams = binding.homeContainer.layoutParams as MarginLayoutParams
            val libraryContainerLayoutParams = binding.libraryContainer.layoutParams as MarginLayoutParams

            homeContainerLayoutParams.updateMargins(bottom = containerMarginSize)
            libraryContainerLayoutParams.updateMargins(bottom = containerMarginSize)

            val playerBottomSheetPeekHeight =
                if (binding.bottomNav != null) {
                    bottomNavHeightWithNav + miniPlayerHeight
                } else {
                    systemNavigationBarHeight + miniPlayerHeight
                }

            binding.mainContent.updateLayoutParams<MarginLayoutParams> {
                if (displayCutout != null) {
                    updateMargins(left = displayCutout.safeInsetLeft, right = displayCutout.safeInsetRight)
                }
            }

            if (::playerBottomSheetBehavior.isInitialized) {
                playerBottomSheetBehavior.peekHeight = playerBottomSheetPeekHeight
            }

            windowInsets
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setupMiniPlayer() {
        binding.miniPlayerComposeView?.apply {
            setContent {
                val windowSizeClass = calculateWindowSizeClass(this@MainActivity)

                Mdc3Theme {
                    MiniPlayer(windowSizeClass = windowSizeClass)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setupPlayerScreen() {
        binding.playerScreenComposeView.apply {
            setContent {
                val windowSizeClass = calculateWindowSizeClass(this@MainActivity)

                Mdc3Theme {
                    PlayerScreen(windowSizeClass = windowSizeClass)
                }
            }
        }
    }

    private fun setupPlayerBottomSheet() {
        if (binding.playerBottomSheet != null) {
            playerBottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet!!)
            playerBottomSheetBehavior.addBottomSheetCallback(playerBottomSheetCallback)

            binding.miniPlayerComposeView?.setOnClickListener {
                playerBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun setupSlideTransition(slideOffset: Float) {
        if (slideOffset < 0) {
            return
        }

        val bottomNavTransitionVelocity = 450
        val transitionOffsetThreshold = 0.15f

        binding.miniPlayerComposeView?.alpha = 1 - (slideOffset / transitionOffsetThreshold)
        binding.miniPlayerComposeView?.isGone = slideOffset == 1f
        binding.bottomNav?.translationY = slideOffset * bottomNavTransitionVelocity
        binding.bottomNav?.alpha = 1 - slideOffset
        binding.playerScreenComposeView.isGone = slideOffset == 0f
        binding.playerScreenComposeView.alpha = (slideOffset - transitionOffsetThreshold) / transitionOffsetThreshold
    }

    private fun setupBottomTabs() {
        val navigationView: NavigationBarView =
            findViewById(R.id.bottom_nav) ?: findViewById(R.id.rail_nav)

        navigationController = HotwireNavigationController(this, navigationView)
        navigationController.load(mainTabs, viewModel.selectedTabIndex)
        navigationController.setOnTabSelectedListener { index, _ ->
            viewModel.selectedTabIndex = index
        }
    }

    private fun switchToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)
    }
}
