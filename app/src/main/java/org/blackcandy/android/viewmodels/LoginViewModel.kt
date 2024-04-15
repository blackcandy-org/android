package org.blackcandy.android.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.blackcandy.android.R
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.SystemInfoRepository
import org.blackcandy.android.data.UserRepository
import org.blackcandy.android.models.AlertMessage
import org.blackcandy.android.models.User
import org.blackcandy.android.utils.TaskResult

data class LoginUiState(
    val serverAddress: String? = null,
    val alertMessage: AlertMessage? = null,
    val email: String = "",
    val password: String = "",
    val currentUser: User? = null,
)

class LoginViewModel(
    private val systemInfoRepository: SystemInfoRepository,
    private val serverAddressRepository: ServerAddressRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState =
        combine(
            _uiState,
            serverAddressRepository.getServerAddressFlow(),
            userRepository.getCurrentUserFlow(),
        ) { state, serverAddress, currentUser ->
            state.copy(
                serverAddress = state.serverAddress ?: serverAddress,
                currentUser = currentUser,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LoginUiState(),
        )

    fun updateServerAddress(serverAddress: String) {
        _uiState.update { it.copy(serverAddress = serverAddress) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun checkSystemInfo(onSuccess: () -> Unit) {
        var serverAddress = uiState.value.serverAddress ?: return

        if (!Regex("^https?://.*").matches(serverAddress)) {
            serverAddress = "http://$serverAddress"
        }

        if (!Patterns.WEB_URL.matcher(serverAddress).matches()) {
            _uiState.update { it.copy(alertMessage = AlertMessage.StringResource(R.string.invalid_server_address)) }
            return
        }

        viewModelScope.launch {
            serverAddressRepository.updateServerAddress(serverAddress)

            when (val result = systemInfoRepository.getSystemInfo()) {
                is TaskResult.Success -> {
                    if (!result.data.isSupported) {
                        _uiState.update { it.copy(alertMessage = AlertMessage.StringResource(R.string.unsupported_server)) }
                    } else {
                        onSuccess()
                    }
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            when (val result = userRepository.login(uiState.value.email, uiState.value.password)) {
                is TaskResult.Success -> Unit

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun alertMessageShown() {
        _uiState.update { it.copy(alertMessage = null) }
    }
}
