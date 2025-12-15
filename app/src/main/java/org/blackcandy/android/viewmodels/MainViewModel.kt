package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import org.blackcandy.android.fragments.navs.LibraryNavHostFragment
import org.blackcandy.shared.data.UserRepository

class MainViewModel(
    userRepository: UserRepository,
) : ViewModel() {
    val currentUserFlow = userRepository.getCurrentUserFlow()

    // Declare the library nav host fragment in view model to prevent it from being recreated when configuration changed.
    val libraryNav = LibraryNavHostFragment()
}
