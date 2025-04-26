package com.near_reality.scripts.npc.actions

import com.near_reality.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCActionCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, body = {
        defaultImports("com.zenyte.game.world.entity.npc.actions.*")
    })