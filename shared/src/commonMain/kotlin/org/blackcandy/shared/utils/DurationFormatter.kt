package org.blackcandy.shared.utils

import kotlin.time.Duration.Companion.seconds

class DurationFormatter {
    companion object {
        fun string(duration: Double): String {
            duration.seconds.toComponents { minutes, seconds, _ ->
                val mm = minutes.toString().padStart(2, '0')
                val ss = seconds.toString().padStart(2, '0')

                return "$mm:$ss"
            }
        }
    }
}
