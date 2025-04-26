package com.near_reality.api.util

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

fun<T> completeWithTimeout(time: Duration, block: CompletableFuture<T>.() -> Unit): T? {
    val future = CompletableFuture<T>()
    block(future)
    return future.get(time.inWholeMilliseconds, TimeUnit.MILLISECONDS)
}


