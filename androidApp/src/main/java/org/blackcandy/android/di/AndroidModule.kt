package org.blackcandy.android.di

import org.blackcandy.android.viewmodels.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidModule =
    module {
        viewModel { MainViewModel(get(), get()) }
    }
