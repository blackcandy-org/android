package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.UserRepository

class AccountSheetViewModel(
    private val userRepository: UserRepository,
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    val currentUserFlow = userRepository.getCurrentUserFlow()

    val serverAddress = runBlocking {
        serverAddressRepository.getServerAddress()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.removeCurrentUser()
            userRepository.removeUserCookies()
        }
    }
}
