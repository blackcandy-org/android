package org.blackcandy.android

import android.os.Bundle
import android.view.MenuItem
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import dev.hotwire.turbo.session.TurboSessionNavHostFragment

class MainActivity : AppCompatActivity(), TurboActivity, OnItemSelectedListener {
    override lateinit var delegate: TurboActivityDelegate
    lateinit var homeNav: TurboSessionNavHostFragment
    lateinit var libraryNav: TurboSessionNavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delegate = TurboActivityDelegate(this, R.id.home_nav_host)
        homeNav = delegate.registerNavHostFragment(R.id.home_nav_host)
        libraryNav = delegate.registerNavHostFragment(R.id.library_nav_host)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val tabSwitcher = findViewById<ViewFlipper>(R.id.nav_switcher)

        return when (item.itemId) {
            R.id.nav_menu_home -> {
                delegate.currentNavHostFragmentId = homeNav.id
                tabSwitcher.displayedChild = 0
                true
            }
            R.id.nav_menu_library -> {
                delegate.currentNavHostFragmentId = libraryNav.id
                tabSwitcher.displayedChild = 1
                true
            }
            else -> false
        }
    }
}
