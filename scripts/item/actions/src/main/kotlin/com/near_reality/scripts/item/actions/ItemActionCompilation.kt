package com.near_reality.scripts.item.actions

import com.near_reality.scripts.item.ItemScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * @author Jire
 */
object ItemActionCompilation : ScriptCompilationConfiguration(
    ItemScriptCompilation
) {

    private fun readResolve(): Any = ItemActionCompilation

}