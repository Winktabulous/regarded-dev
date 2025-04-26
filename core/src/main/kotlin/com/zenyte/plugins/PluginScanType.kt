package com.zenyte.plugins

import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult

/**
 * @author Jire
 */
fun interface PluginScanType {

    fun scan(result: ScanResult, scanClass: Class<*>): ClassInfoList

}