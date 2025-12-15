package org.blackcandy.shared.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.blackcandy.shared.data.ServerAddressRepository
import org.blackcandy.shared.data.SystemInfoRepository
import org.blackcandy.shared.data.UserRepository
import org.blackcandy.shared.models.AlertMessage
import org.blackcandy.shared.models.User
import org.blackcandy.shared.utils.TaskResult

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

        if (!isValidUrl(serverAddress)) {
            _uiState.update { it.copy(alertMessage = AlertMessage.LocalizedString(AlertMessage.DefinedMessages.INVALID_SERVER_ADDRESS)) }
            return
        }

        viewModelScope.launch {
            serverAddressRepository.updateServerAddress(serverAddress)

            when (val result = systemInfoRepository.getSystemInfo()) {
                is TaskResult.Success -> {
                    if (!result.data.isSupported) {
                        _uiState.update {
                            it.copy(
                                alertMessage = AlertMessage.LocalizedString(AlertMessage.DefinedMessages.UNSUPPORTED_SERVER),
                            )
                        }
                    } else {
                        val responseServerAddress = result.data.serverAddress

                        if (responseServerAddress != null && responseServerAddress != serverAddress) {
                            serverAddressRepository.updateServerAddress(responseServerAddress)
                        }

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
                is TaskResult.Success -> {
                    Unit
                }

                is TaskResult.Failure -> {
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(result.message)) }
                }
            }
        }
    }

    fun alertMessageShown() {
        _uiState.update { it.copy(alertMessage = null) }
    }

    private fun isValidUrl(url: String): Boolean {
        val urlRegex = Regex("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
        return urlRegex.matches(url)
    }
}
