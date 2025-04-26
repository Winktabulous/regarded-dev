@file:JvmName("ReferenceCountedExtensions")

package com.near_reality.buffer

import io.netty.util.ReferenceCounted

inline fun <T : ReferenceCounted?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        this?.release()
    }
}