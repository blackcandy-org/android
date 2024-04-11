package org.blackcandy.android

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updatePadding
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.compose.player.MiniPlayer
import org.blackcandy.android.compose.player.PlayerScreen
import org.blackcandy.android.databinding.ActivityMainBinding
import org.blackcandy.android.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), TurboActivity, OnItemSelectedListener {
    companion object {
        private const val SELECTED_NAV_ITEM_ID_KEY = "selected_nav_item_id"
    }

    private val viewModel: MainViewModel by viewModel()
    private var windowInsets: WindowInsetsCompat? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var playerBottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    override lateinit var delegate: TurboActivityDelegate

    private val isCompactView: Boolean
        get() {
            val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
            val width = metrics.bounds.width()
            val height = metrics.bounds.height()
            val density = resources.displayMetrics.density
            val windowSizeClass = WindowSizeClass.compute(width / density, height / density)

            return windowSizeClass.windowWidthSizeClass === WindowWidthSizeClass.COMPACT
        }

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
        delegate = TurboActivityDelegate(this, R.id.home_container)

        viewModel.setupMusicServiceController()

        setupLayout()
        setupNavListener()
        setupPlayerBottomSheet()
        setupMiniPlayer()
        setupPlayerScreen()
        restoreSavedState(savedInstanceState)

        setContentView(binding.root)
    }

    override fun onRestart() {
        super.onRestart()

        viewModel.getCurrentPlaylist()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::playerBottomSheetBehavior.isInitialized) {
            playerBottomSheetBehavior.removeBottomSheetCallback(playerBottomSheetCallback)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the selected nav item id to restore it when configuration changed.
        binding.bottomNav?.let { outState.putInt(SELECTED_NAV_ITEM_ID_KEY, it.selectedItemId) }
        binding.railNav?.let { outState.putInt(SELECTED_NAV_ITEM_ID_KEY, it.selectedItemId) }

        super.onSaveInstanceState(outState)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_menu_home, R.id.nav_menu_library -> {
                showSelectedNavItem(item.itemId)
                true
            }

            else -> false
        }
    }

    private fun requireLogin(): Boolean {
        runBlocking { viewModel.currentUserFlow.first() } ?: return true

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

    private fun setupNavListener() {
        binding.bottomNav?.setOnItemSelectedListener(this)
        binding.railNav?.setOnItemSelectedListener(this)
    }

    private fun setupLayout() {
        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            windowInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            insets
        }

        binding.root.post {
            // Because displaying edge-to-edge, so the height of bottom nav includes the height of system navigation bar.
            val bottomNavHeightWithNav = binding.bottomNav?.height ?: 0
            val systemNavigationBarHeight = windowInsets?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: 0
            val bottomNavHeight = if (binding.bottomNav != null) bottomNavHeightWithNav - systemNavigationBarHeight else 0
            val miniPlayerHeight =
                if (binding.miniPlayerComposeView != null) {
                    resources.getDimensionPixelSize(
                        R.dimen.mini_player_height,
                    )
                } else {
                    0
                }

            val containerPaddingSize =
                when (true) {
                    (binding.bottomNav != null && binding.miniPlayerComposeView != null) -> bottomNavHeightWithNav + miniPlayerHeight
                    (binding.bottomNav == null && binding.miniPlayerComposeView != null) -> systemNavigationBarHeight + miniPlayerHeight
                    (binding.bottomNav == null && binding.miniPlayerComposeView == null) -> systemNavigationBarHeight
                    else -> 0
                }

            binding.homeContainer.updatePadding(bottom = containerPaddingSize)
            binding.libraryContainer.updatePadding(bottom = containerPaddingSize)

            if (::playerBottomSheetBehavior.isInitialized) {
                playerBottomSheetBehavior.peekHeight = bottomNavHeight + miniPlayerHeight
            }
        }

        requestedOrientation =
            if (isCompactView) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR
            }

        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun setupMiniPlayer() {
        binding.miniPlayerComposeView?.apply {
            setContent {
                Mdc3Theme {
                    MiniPlayer()
                }
            }
        }
    }

    private fun setupPlayerScreen() {
        binding.playerScreenComposeView.apply {
            setContent {
                Mdc3Theme {
                    PlayerScreen()
                }
            }
        }
    }

    private fun setupPlayerBottomSheet() {
        if (binding.playerBottomSheet != null) {
            playerBottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet!!)
            playerBottomSheetBehavior.addBottomSheetCallback(playerBottomSheetCallback)
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

    private fun restoreSavedState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            showSelectedNavItem(savedInstanceState.getInt(SELECTED_NAV_ITEM_ID_KEY, R.id.nav_menu_home))
        }
    }

    private fun switchToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)
    }

    private fun showSelectedNavItem(itemId: Int) {
        when (itemId) {
            R.id.nav_menu_home -> {
                binding.homeContainer.isGone = false
                binding.libraryContainer.isGone = true
                delegate.currentNavHostFragmentId = R.id.home_container
            }

            R.id.nav_menu_library -> {
                val libraryNavFragment =
                    supportFragmentManager.findFragmentById(viewModel.libraryNav.id)

                // Lazily add the library nav host fragment.
                if (libraryNavFragment == null) {
                    supportFragmentManager.commitNow {
                        add(R.id.library_container, viewModel.libraryNav)
                    }

                    delegate.registerNavHostFragment(R.id.library_container)
                }

                binding.homeContainer.isGone = true
                binding.libraryContainer.isGone = false
                delegate.currentNavHostFragmentId = R.id.library_container
            }
        }
    }
}
