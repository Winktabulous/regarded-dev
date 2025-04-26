package com.zenyte.net.io

infix fun Short.xor(mask: Int): Int = toInt() xor mask
infix fun Byte.shr(mask: Int): Int = toInt() shr mask
infix fun Byte.shl(mask: Int): Int = toInt() shl mask
infix fun Byte.and(mask: Int): Int = toInt() and mask
infix fun Short.and(mask: Int): Int = toInt() and mask