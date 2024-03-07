package org.blackcandy.android.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.blackcandy.android.R

enum class PlaybackMode {
    NO_REPEAT,
    REPEAT,
    REPEAT_ONE,
    SHUFFLE,
    ;

    @get:DrawableRes
    val iconResourceId get() =
        when (this) {
            NO_REPEAT -> R.drawable.baseline_repeat_24
            REPEAT -> R.drawable.baseline_repeat_24
            REPEAT_ONE -> R.drawable.baseline_repeat_one_24
            SHUFFLE -> R.drawable.baseline_shuffle_24
        }

    @get:StringRes
    val titleResourceId get() =
        when (this) {
            NO_REPEAT -> R.string.no_repeat_mode
            REPEAT -> R.string.repeat_mode
            REPEAT_ONE -> R.string.repeat_one_mode
            SHUFFLE -> R.string.shuffle_mode
        }

    val next get() = PlaybackMode.values()[(this.ordinal + 1) % PlaybackMode.values().size]
}
