package com.near_reality.scripts.item.equip

import com.near_reality.scripts.item.ItemScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * Represents the compilation configuration for [equip scripts][ItemEquipScript].
 *
 * @author Stan van der Bend
 */
object ItemEquipCompilation : ScriptCompilationConfiguration(
    ItemScriptCompilation, body = {
        defaultImports("")
    }
)
