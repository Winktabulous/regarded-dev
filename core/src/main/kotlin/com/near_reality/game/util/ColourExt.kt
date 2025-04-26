package com.near_reality.game.util

import com.zenyte.game.util.Colour

operator fun Colour.invoke(string: String): String = wrap(string)
operator fun Colour.invoke(number: Number) = invoke("$number")
