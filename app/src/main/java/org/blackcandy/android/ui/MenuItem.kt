package org.blackcandy.android.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuItem(
    val id: String,
    @StringRes val titleResourceId: Int,
    @DrawableRes val iconResourceId: Int,
    val action: () -> Unit = {},
)
