package rk.core

import java.util.concurrent.TimeUnit

fun readableDuration(millis: Long) =
    buildString {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        if (minutes >= 10) {
            append(minutes)
        } else {
            append("0$minutes")
        }
        append(":")
        if (seconds >= 10) {
            append(seconds)
        } else {
            append("0$seconds")
        }
    }