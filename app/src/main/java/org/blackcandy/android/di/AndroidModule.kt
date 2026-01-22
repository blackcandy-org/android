package org.blackcandy.android.di

import org.blackcandy.android.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule =
    module {
        // TODO: rename MainViewModel and remove it the shared module.
        // Tha main purpose of MainViewModel is to get current user
        // and declare the library nav host fragment in view model to prevent it from being recreated when configuration changed.
        // After we migrate to use turbo native. it already provide a way to declare bottom tag. so we don't need to declare the library nav host fragment
        // in viewmodel. and only purpose of left for MainViewModel will be get current user. should rename a proper name.
        // After refactoring. we may also don't need this androidModule anymore.
        viewModel { MainViewModel(get(), get()) }
    }
