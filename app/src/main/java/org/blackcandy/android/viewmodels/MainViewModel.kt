package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.blackcandy.shared.data.ServerAddressRepository
import org.blackcandy.shared.data.UserRepository

class MainViewModel(
    userRepository: UserRepository,
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    val currentUserFlow = userRepository.getCurrentUserFlow()

    var selectedTabIndex = 0

    val serverAddress =
        runBlocking {
            serverAddressRepository.getServerAddress()
        }
}
