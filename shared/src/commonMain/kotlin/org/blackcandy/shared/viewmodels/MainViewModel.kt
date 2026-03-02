package org.blackcandy.shared.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.blackcandy.shared.data.ServerAddressRepository
import org.blackcandy.shared.data.UserRepository

class MainViewModel(
    private val userRepository: UserRepository,
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    var selectedTabIndex = 0

    val currentUserFlow = userRepository.getCurrentUserFlow()

    val currentUser =
        runBlocking {
            userRepository.getCurrentUser()
        }

    val serverAddress =
        runBlocking {
            serverAddressRepository.getServerAddress()
        }
}
