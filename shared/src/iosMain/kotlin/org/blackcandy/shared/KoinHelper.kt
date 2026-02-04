package org.blackcandy.shared

import org.blackcandy.shared.di.appModule
import org.blackcandy.shared.viewmodels.LoginViewModel
import org.blackcandy.shared.viewmodels.MainViewModel
import org.blackcandy.shared.viewmodels.WebViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule())
    }
}

class KoinHelper : KoinComponent {
    fun getMainViewModel(): MainViewModel = get()

    fun getLoginViewModel(): LoginViewModel = get()

    fun getWebViewModel(): WebViewModel = get()
}
