package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.data.UserRepository

class MainViewModel(
    userRepository: UserRepository,
) : ViewModel() {
    val currentUserFlow = userRepository.getCurrentUserFlow()
    val currentUser =
        runBlocking {
            currentUserFlow.first()
        }
}
