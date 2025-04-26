package com.zenyte.game.util

infix fun Int.component(id: Int) = this shl 16 or id