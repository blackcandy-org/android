package org.blackcandy.android.viewmodels

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.R
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.SystemInfoRepository
import javax.inject.Inject

data class LoginUiState(
    val serverAddress: String = "",
    val userMessage: Int? = null,
    val loginRoute: LoginRoute = LoginRoute.Connection,
)

enum class LoginRoute(@StringRes val title: Int) {
    Connection(R.string.connection_title),
    Authentication(R.string.authentication_title),
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val systemInfoRepository: SystemInfoRepository,
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    private val serverAddress = runBlocking {
        serverAddressRepository.getServerAddress()
    }

    private val _uiState = MutableStateFlow(LoginUiState(serverAddress = serverAddress))

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateServerAddress(serverAddress: String) {
        _uiState.update { it.copy(serverAddress = serverAddress) }
    }

    fun checkSystemInfo() {
        var serverAddress = uiState.value.serverAddress

        if (!Regex("^https?://.*").matches(serverAddress)) {
            serverAddress = "http://$serverAddress"
        }

        if (!Patterns.WEB_URL.matcher(serverAddress).matches()) {
            _uiState.update { it.copy(userMessage = R.string.invalid_server_address) }
            return
        }

        viewModelScope.launch {
            serverAddressRepository.updateServerAddress(serverAddress)

            val systemInfo = systemInfoRepository.getSystemInfo()

            if (!systemInfo.isSupported) {
                _uiState.update { it.copy(userMessage = R.string.unsupported_server) }
            } else {
                _uiState.update { it.copy(loginRoute = LoginRoute.Authentication) }
            }
        }
    }

    fun snackbarMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun updateLoginRoute(route: LoginRoute) {
        _uiState.update { it.copy(loginRoute = route) }
    }
}
