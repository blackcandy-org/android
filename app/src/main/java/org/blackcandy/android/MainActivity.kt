package org.blackcandy.android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.compose.player.MiniPlayer
import org.blackcandy.android.compose.player.PlayerScreen
import org.blackcandy.android.databinding.ActivityMainBinding
import org.blackcandy.android.fragments.navs.HomeNavHostFragment
import org.blackcandy.android.fragments.navs.LibraryNavHostFragment
import org.blackcandy.android.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), TurboActivity, OnItemSelectedListener {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private var windowInsets: WindowInsetsCompat? = null
    override lateinit var delegate: TurboActivityDelegate
    lateinit var homeNav: TurboSessionNavHostFragment
    lateinit var libraryNav: TurboSessionNavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireLogin()

        binding = ActivityMainBinding.inflate(layoutInflater)
        homeNav = HomeNavHostFragment()
        libraryNav = LibraryNavHostFragment()

        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            windowInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            insets
        }

        initHome()

        delegate = TurboActivityDelegate(this, homeNav.id)

        setupLayout()
        setupBottomNav()
        setupMiniPlayer()
        setupPlayerScreen()

        setContentView(binding.root)

        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_menu_home -> {
                supportFragmentManager.commit {
                    hide(libraryNav)
                    show(homeNav)
                }

                delegate.currentNavHostFragmentId = homeNav.id

                true
            }

            R.id.nav_menu_library -> {
                val libraryNavFragment = supportFragmentManager.findFragmentById(libraryNav.id)

                if (libraryNavFragment == null) {
                    supportFragmentManager.commitNow {
                        add(R.id.main_container, libraryNav)
                        hide(homeNav)
                    }

                    delegate.registerNavHostFragment(libraryNav.id)
                } else {
                    supportFragmentManager.commit {
                        hide(homeNav)
                        show(libraryNav)
                    }
                }

                delegate.currentNavHostFragmentId = libraryNav.id

                true
            }
            else -> false
        }
    }

    private fun requireLogin() {
        val currentUser = runBlocking { viewModel.currentUserFlow.first() }

        if (currentUser == null) {
            switchToLoginActivity()
            return
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUserFlow.collect {
                    if (it == null) {
                        switchToLoginActivity()
                    }
                }
            }
        }
    }

    private fun initHome() {
        supportFragmentManager.commitNow {
            add(R.id.main_container, homeNav)
        }
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(this)
    }

    private fun setupLayout() {
        binding.bottomNav.post {
            val playerBottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet)
            val systemNavigationBarHeight = getSystemNavigationBarHeight()

            // Because displaying edge-to-edge, so the height of bottom nav includes the height of system navigation bar.
            val bottomNavHeightWithNav = binding.bottomNav.height

            val miniPlayerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
            val bottomNavHeight = if (binding.bottomNav.isVisible) bottomNavHeightWithNav - systemNavigationBarHeight else 0

            playerBottomSheetBehavior.peekHeight = bottomNavHeight + miniPlayerHeight
            binding.mainContainer.updatePadding(bottom = bottomNavHeight + miniPlayerHeight)
        }
    }

    private fun setupMiniPlayer() {
        binding.miniPlayerComposeView.apply {
            setContent {
                MiniPlayer()
            }
        }
    }

    private fun setupPlayerScreen() {
        binding.playerScreenComposeView.apply {
            setContent {
                PlayerScreen()
            }
        }
    }

    private fun switchToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)
    }

    private fun getSystemNavigationBarHeight(): Int {
        return windowInsets?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: 0
    }
}
