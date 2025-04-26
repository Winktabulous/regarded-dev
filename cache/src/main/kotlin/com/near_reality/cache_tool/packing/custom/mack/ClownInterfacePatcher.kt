package com.near_reality.cache_tool.packing.custom.mack

import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache
import mgi.tools.jagcached.cache.File
import mgi.types.component.ComponentDefinitions

class ClownInterfacePatcher(val cache: Cache = CacheManager.getCache()!!) {

    fun patchHooks(
        widgetId: Int,
        scriptIdMap: Map<Int, Int> = emptyMap(),
        widgetIdMap: Map<Int, Int> = emptyMap()
    ) {
        println("Patching script id references for widget $widgetId")
        val widgetGroup = cache.widgetArchive.findGroupByID(widgetId)
        for (componentId in 0 until widgetGroup.highestFileId) {
            val file: File = widgetGroup.findFileByID(componentId) ?: continue
            val buffer = file.data ?: continue
            buffer.position = 0
            val id: Int = widgetId shl 16 or componentId
            val component =  ComponentDefinitions(id, buffer)
            component.onLoadListener?.patchListener("onLoadListener", scriptIdMap, widgetIdMap)
            component.onClickListener?.patchListener("onClickListener", scriptIdMap, widgetIdMap)
            component.onClickRepeatListener?.patchListener("onClickRepeatListener", scriptIdMap, widgetIdMap)
            component.onReleaseListener?.patchListener("onReleaseListener", scriptIdMap, widgetIdMap)
            component.onHoldListener?.patchListener("onHoldListener", scriptIdMap, widgetIdMap)
            component.onMouseOverListener?.patchListener("onMouseOverListener", scriptIdMap, widgetIdMap)
            component.onMouseRepeatListener?.patchListener("onMouseRepeatListener", scriptIdMap, widgetIdMap)
            component.onMouseLeaveListener?.patchListener("onMouseLeaveListener", scriptIdMap, widgetIdMap)
            component.onDragListener?.patchListener("onDragListener", scriptIdMap, widgetIdMap)
            component.onDragCompleteListener?.patchListener("onDragCompleteListener", scriptIdMap, widgetIdMap)
            component.onTargetEnterListener?.patchListener("onTargetEnterListener", scriptIdMap, widgetIdMap)
            component.onTargetLeaveListener?.patchListener("onTargetLeaveListener", scriptIdMap, widgetIdMap)
            component.onVarTransmitListener?.patchListener("onVarTransmitListener", scriptIdMap, widgetIdMap)
            file.data = component.encode()
            if (file.isDataChanged) {
                println("Patched component $id")
            }
        }
        widgetGroup.finish()
    }

    private fun Array<Any>.patchListener(name: String, scriptIdMap: Map<Int, Int>, widgetIdMap: Map<Int, Int>) {
        if (isNotEmpty()) {
            val scriptId = this[0]
            if (scriptIdMap.containsKey(scriptId)) {
                val newValue = scriptIdMap[scriptId]!!
                this[0] = newValue
                println("listener[$name], mapped script id `$scriptId` to `$newValue`")
            } else {
                println("listener[$name], script id `$scriptId` not found in map")
            }
            if (size > 1) {
                for (i in 1 until size) {
                    val value = this[i]
                    if (value is Int) {
                        val maybeWidgetId = value shr 16
                        val maybeComponentId = value and 0xFFFF
                        val maybeNewWidgetId = widgetIdMap[maybeWidgetId]
                        if (maybeNewWidgetId != null) {
                            this[i] = maybeNewWidgetId shl 16 or maybeComponentId
                            println("listener[$name], mapped widget id `$maybeWidgetId` to `$maybeNewWidgetId`")
                        }
                    }
                }
            }
        }
    }

    private val Cache.widgetArchive get() = archives[ArchiveType.INTERFACES.id]
}
