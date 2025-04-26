package com.zenyte.plugins

import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult

/**
 * @author Jire
 */
object SuperClassPluginScanType : PluginScanType {

    override fun scan(result: ScanResult, scanClass: Class<*>): ClassInfoList =
        result.getSubclasses(scanClass)
            .filter { info -> !info.isInterface && !info.isAbstract && !info.isAnonymousInnerClass }

}