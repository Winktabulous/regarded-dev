package com.zenyte.plugins

import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult

/**
 * @author Jire
 */
object ImplementingPluginScanType : PluginScanType {

    override fun scan(result: ScanResult, scanClass: Class<*>): ClassInfoList =
        result.getClassesImplementing(scanClass)
            .filter { info -> !info.isInterface && !info.isAbstract && !info.isAnonymousInnerClass }

}