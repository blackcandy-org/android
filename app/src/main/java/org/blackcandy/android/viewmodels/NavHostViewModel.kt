package org.blackcandy.android.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.blackcandy.android.data.ServerAddressRepository

class NavHostViewModel(
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    val serverAddress =
        runBlocking {
            serverAddressRepository.getServerAddress()
        }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            when (theme) {
                "dark" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                "light" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                "auto" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }
}
