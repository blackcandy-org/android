package org.blackcandy.android.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuItem(
    @StringRes val titleResourceId: Int,
    @DrawableRes val iconResourceId: Int,
    val action: () -> Unit,
)
