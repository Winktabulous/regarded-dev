package com.near_reality.util.logging

import org.slf4j.Logger

abstract class LazyLoggerWithPrefix(
    private val backingLogger : Logger
) {

    fun info(provider: () -> String) {
        if (showInfoLogs())
            backingLogger.info(prefixProvider() + provider())
    }

    fun warn(provider: () -> String) {
        if (showWarningLogs())
            backingLogger.warn(prefixProvider() + provider())
    }

    fun debug(provider: () -> String) {
        if (showDebugLogs())
            backingLogger.debug(prefixProvider() + provider())
    }

    fun trace(provider: () -> String) {
        if (showTraceLogs())
            backingLogger.trace(prefixProvider() + provider())
    }

    fun error(cause: Throwable, provider: () -> String) {
        backingLogger.error(prefixProvider() + provider(), cause)
    }

    fun error(provider: () -> String) {
        backingLogger.error(prefixProvider() + provider())
    }

    open fun prefixProvider() = ""
    open fun showInfoLogs() = backingLogger.isInfoEnabled
    open fun showWarningLogs() = backingLogger.isWarnEnabled
    open fun showDebugLogs() = backingLogger.isDebugEnabled
    open fun showTraceLogs() = backingLogger.isTraceEnabled
}
