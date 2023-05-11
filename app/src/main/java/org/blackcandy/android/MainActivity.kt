package org.blackcandy.android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import org.blackcandy.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TurboActivity, OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override lateinit var delegate: TurboActivityDelegate
    lateinit var homeNav: TurboSessionNavHostFragment
    lateinit var libraryNav: TurboSessionNavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        delegate = TurboActivityDelegate(this, R.id.home_nav_host)
        homeNav = delegate.registerNavHostFragment(R.id.home_nav_host)
        libraryNav = delegate.registerNavHostFragment(R.id.library_nav_host)

        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)

        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_menu_home -> {
                delegate.currentNavHostFragmentId = homeNav.id
                binding.navSwitcher.displayedChild = 0
                true
            }
            R.id.nav_menu_library -> {
                delegate.currentNavHostFragmentId = libraryNav.id
                binding.navSwitcher.displayedChild = 1
                true
            }
            else -> false
        }
    }
}
