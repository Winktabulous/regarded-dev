package com.near_reality.api.util

fun String.formatUsername() =
    lowercase().replace("_", " ")
