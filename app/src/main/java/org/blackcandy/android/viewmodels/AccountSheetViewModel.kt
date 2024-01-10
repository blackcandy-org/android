package org.blackcandy.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.UserRepository
import org.blackcandy.android.models.User

data class AccountSheetUiState(
    val serverAddress: String? = null,
    val currentUser: User? = null,
)

class AccountSheetViewModel(
    private val userRepository: UserRepository,
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    val uiState =
        combine(
            serverAddressRepository.getServerAddressFlow(),
            userRepository.getCurrentUserFlow(),
        ) { serverAddress, currentUser ->
            AccountSheetUiState(
                serverAddress = serverAddress,
                currentUser = currentUser,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AccountSheetUiState(),
        )

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
