package com.near_reality.scripts.npc.actions

import com.near_reality.scripts.npc.NPCScript
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.actions.NPCOptionHandler
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy
import com.zenyte.game.world.entity.player.Player
import it.unimi.dsi.fastutil.ints.IntArraySet
import it.unimi.dsi.fastutil.ints.IntSet
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "NPC Action Script",
    fileExtension = "npcaction.kts",
    compilationConfiguration = NPCActionCompilation::class
)
abstract class NPCActionScript : NPCPlugin(), NPCScript {

    private var handleBody: (() -> Unit)? = null

    fun handle(body: () -> Unit) {
        handleBody = body
    }

    override fun handle() {
        handleBody?.invoke()
    }

    private val npcs: IntSet = IntArraySet()

    fun npcs(vararg npcs: Int) {
        for (npc in npcs)
            this.npcs.add(npc)
    }

    override fun getNPCs(): IntArray = npcs.toIntArray()

    operator fun String.invoke(
        onClick: (NPCOptionHandler.(NPCOption, (Player, NPC) -> Unit) -> Unit)? = null,
        handle: NPCOptionHandler.() -> Unit,
    ) {
        bind(this, object : OptionHandler {
            override fun click(player: Player, npc: NPC, option: NPCOption) {
                if (onClick != null) {
                    NPCOptionHandler(player, npc).onClick(option, ::execute)
                } else
                    super.click(player, npc, option)
            }
            override fun handle(player: Player, npc: NPC) {
                NPCOptionHandler(player, npc).handle()
            }
        })
    }

    /**
     * Used for setting a [Player.routeEvent] that fires the execution even,
     * when the player is at most [distance] away from the interacted with [NPC].
     */
    fun distancedRoute(distance: Int = 1) : NPCOptionHandler.(NPCOption, (Player, NPC) -> Unit) -> Unit {
        return {  _, execute ->
            player.routeEvent = EntityEvent(player, DistancedEntityStrategy(npc, distance),
                { execute(player, npc) }, true)
        }
    }
}
