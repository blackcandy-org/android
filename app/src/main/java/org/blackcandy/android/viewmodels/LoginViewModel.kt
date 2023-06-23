package org.blackcandy.android.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.blackcandy.android.R

data class LoginUiState(
    val serverAddress: String = "",
    val userMessage: Int? = null,
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateServerAddress(serverAddress: String) {
        _uiState.update { it.copy(serverAddress = serverAddress) }
    }

    fun checkSystemInfo() {
        if (!Patterns.WEB_URL.matcher(uiState.value.serverAddress).matches()) {
            _uiState.update { it.copy(userMessage = R.string.invalid_server_address) }
            return
        }
    }

    fun snackbarMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
