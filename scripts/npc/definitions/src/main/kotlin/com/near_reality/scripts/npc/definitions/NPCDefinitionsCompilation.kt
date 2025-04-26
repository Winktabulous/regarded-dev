package com.near_reality.scripts.npc.definitions

import com.near_reality.scripts.CoreCompilation
import com.near_reality.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports


/**
 * @author Jire
 */
object NPCDefinitionsCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, CoreCompilation, body = {
        defaultImports(
            "com.zenyte.game.world.entity.Entity.EntityType",
            "com.zenyte.game.world.entity.Entity.EntityType.*",

            "com.near_reality.game.item.CustomNpcId",
            "com.near_reality.game.item.CustomNpcId.*",

            "com.zenyte.game.world.entity.npc.combatdefs.ImmunityType",
            "com.zenyte.game.world.entity.npc.combatdefs.ImmunityType.*",

            "com.zenyte.game.world.entity.npc.combatdefs.AggressionType",
            "com.zenyte.game.world.entity.npc.combatdefs.AggressionType.*",

            "com.zenyte.game.world.entity.npc.combatdefs.MonsterType",
            "com.zenyte.game.world.entity.npc.combatdefs.MonsterType.*",

            "com.zenyte.game.world.entity.npc.combatdefs.WeaknessType",
            "com.zenyte.game.world.entity.npc.combatdefs.WeaknessType.*",

            "com.zenyte.game.world.entity.Toxins.ToxinType",
            "com.zenyte.game.world.entity.Toxins.ToxinType.*",

            "com.zenyte.game.world.entity.npc.combatdefs.AttackType",
            "com.zenyte.game.world.entity.npc.combatdefs.AttackType.*",
        )
    }
) {
    private fun readResolve(): Any = NPCDefinitionsCompilation
}