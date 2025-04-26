package com.near_reality.scripts.npc.definitions

import com.near_reality.scripts.npc.NPCScript
import com.zenyte.game.world.entity.npc.combatdefs.*
import com.zenyte.plugins.InitPlugin
import com.zenyte.plugins.PluginPriority
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "NPC Definitions Script",
    "npcs.kts",
    compilationConfiguration = NPCDefinitionsCompilation::class
)
@PluginPriority(1_000)
abstract class NPCDefinitionsScript : NPCScript, InitPlugin {

    operator fun Int.invoke(build: NPCCombatDefinitions.() -> Unit) {
        val def = NPCCombatDefinitions().apply {
            id = this@invoke

            build()
        }
        NPCCDLoader.insert(this, def)
    }

    fun NPCCombatDefinitions.stats(build: StatDefinitions.() -> Unit) {
        statDefinitions = (statDefinitions?:StatDefinitions()).apply(build)
    }

    fun NPCCombatDefinitions.attack(build: AttackDefinitions.() -> Unit) {
        attackDefinitions = (attackDefinitions?:AttackDefinitions()).apply(build)
    }

    fun NPCCombatDefinitions.block(build: BlockDefinitions.() -> Unit) {
        blockDefinitions = (blockDefinitions?:BlockDefinitions()).apply(build)
    }

    fun NPCCombatDefinitions.spawn(build: SpawnDefinitions.() -> Unit) {
        spawnDefinitions = (spawnDefinitions?:SpawnDefinitions()).apply(build)
    }
}
