package com.zenyte.plugins

/**
 * @author Jire
 */
enum class PluginScanTypes(private val scanType: PluginScanType) : PluginScanType by scanType {

    IMPLEMENTING(ImplementingPluginScanType),
    SUPERCLASS(SuperClassPluginScanType),
    ANNOTATED(AnnotatedPluginScanType),
    METHOD_ANNOTATED(MethodAnnotatedPluginScanType);

}