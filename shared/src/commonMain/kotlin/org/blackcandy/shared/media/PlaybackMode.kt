package org.blackcandy.shared.media

enum class PlaybackMode {
    NO_REPEAT,
    REPEAT,
    REPEAT_ONE,
    SHUFFLE,
    ;

    val next get() = values()[(this.ordinal + 1) % values().size]
}
