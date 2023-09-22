package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import org.blackcandy.android.data.UserRepository

class MainViewModel(
    userRepository: UserRepository,
) : ViewModel() {
    val currentUserFlow = userRepository.getCurrentUserFlow()
}
