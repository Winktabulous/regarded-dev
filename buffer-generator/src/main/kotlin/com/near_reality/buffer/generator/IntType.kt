package com.near_reality.buffer.generator

import com.near_reality.util.capitalize
import kotlin.reflect.KClass

enum class IntType(
    val width: Int,
    private val signedReadType: KClass<*>,
    private val unsignedReadType: KClass<*>,
    val writeType: KClass<*>
) {
    BYTE(1, Byte::class, Short::class, Int::class),
    SHORT(2, Short::class, Int::class, Int::class),
    INT(4, Int::class, Long::class, Int::class);

    val prettyName: String = name.lowercase().capitalize()

    fun getReadType(signedness: Signedness): KClass<*> {
        return if (signedness == Signedness.SIGNED) {
            signedReadType
        } else {
            unsignedReadType
        }
    }
}
