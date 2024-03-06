package org.blackcandy.android.utils

import kotlin.time.Duration.Companion.seconds

class DurationFormatter {
    companion object {
        fun string(duration: Double): String {
            duration.seconds.toComponents { minutes, seconds, _ ->
                return "%02d:%02d".format(minutes, seconds)
            }
        }
    }
}
