package org.blackcandy.shared.models

import androidx.annotation.StringRes

sealed class AlertMessage {
    data class String(val value: kotlin.String?) : AlertMessage()

    data class StringResource(
        @StringRes val value: Int,
    ) : AlertMessage()
}
