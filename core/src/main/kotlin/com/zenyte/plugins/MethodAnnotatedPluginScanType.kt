package com.zenyte.plugins

import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult

/**
 * @author Jire
 */
object MethodAnnotatedPluginScanType : PluginScanType {

    override fun scan(result: ScanResult, scanClass: Class<*>): ClassInfoList {
        if (!scanClass.isAnnotation) throw IllegalArgumentException()

        @Suppress("UNCHECKED_CAST")
        return result.getClassesWithMethodAnnotation(scanClass as Class<out Annotation>)
    }

}