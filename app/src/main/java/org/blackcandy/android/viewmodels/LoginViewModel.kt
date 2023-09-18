package org.blackcandy.android.viewmodels

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.R
import org.blackcandy.android.api.ApiException
import org.blackcandy.android.data.ServerAddressRepository
import org.blackcandy.android.data.SystemInfoRepository
import org.blackcandy.android.models.AlertMessage

data class LoginUiState(
    val serverAddress: String = "",
    val alertMessage: AlertMessage? = null,
    val loginRoute: LoginRoute = LoginRoute.Connection,
)

enum class LoginRoute(@StringRes val title: Int) {
    Connection(R.string.connection_title),
    Authentication(R.string.authentication_title),
}

class LoginViewModel(
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
            _uiState.update { it.copy(alertMessage = AlertMessage.StringResource(R.string.invalid_server_address)) }
            return
        }

        viewModelScope.launch {
            serverAddressRepository.updateServerAddress(serverAddress)

            try {
                val systemInfo = systemInfoRepository.getSystemInfo()

                if (!systemInfo.isSupported) {
                    _uiState.update { it.copy(alertMessage = AlertMessage.StringResource(R.string.unsupported_server)) }
                } else {
                    _uiState.update { it.copy(loginRoute = LoginRoute.Authentication) }
                }
            } catch (exception: ApiException) {
                exception.message?.let { message ->
                    _uiState.update { it.copy(alertMessage = AlertMessage.String(message)) }
                }
            }
        }
    }

    fun snackbarMessageShown() {
        _uiState.update { it.copy(alertMessage = null) }
    }

    fun updateLoginRoute(route: LoginRoute) {
        _uiState.update { it.copy(loginRoute = route) }
    }
}
