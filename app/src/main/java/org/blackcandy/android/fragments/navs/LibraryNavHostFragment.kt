package org.blackcandy.android.fragments.navs

class LibraryNavHostFragment : MainNavHostFragment() {
    override val sessionName = "library"
    override val startLocation get() = "${super.startLocation}/library"
}
