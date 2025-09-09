package org.blackcandy.shared.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.blackcandy.shared.data.ServerAddressRepository

class HomeViewModel(
    private val serverAddressRepository: ServerAddressRepository,
) : ViewModel() {
    val serverAddress =
        runBlocking {
            serverAddressRepository.getServerAddress()
        }
}
