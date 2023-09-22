package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.blackcandy.android.data.UserRepository

class AccountSheetViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userRepository.removeCurrentUser()
            userRepository.removeUserCookies()
        }
    }
}
