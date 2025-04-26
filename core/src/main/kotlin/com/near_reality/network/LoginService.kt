package com.near_reality.network

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object LoginService {

    @JvmStatic val service: ExecutorService =
            Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() - 2,
                ThreadFactoryBuilder()
                    .setPriority(Thread.MIN_PRIORITY + 1)
                    .setNameFormat("${LoginService::class.simpleName}-%d")
                    .build()
            )
}