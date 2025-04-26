package com.near_reality.game.content.pvm_arena.npc

import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcHealthBarHudManager.showHud
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcHealthBarHudManager.update
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import java.lang.ref.WeakReference
import java.util.*

/**
 * Manages the dynamic switching and updating of HP Huds for players in the PVM Arena.
 * This has to be used because normally only one NPC can be tracked by the HP Hud.
 *
 * When a player attacks a [NPC] they will [show the hud][showHud] for that NPC,
 * which will be [updated][update] every time the [NPC] takes damage.
 *
 * @author Stan van der Bend
 */
internal object PvmArenaNpcHealthBarHudManager {

    private val npcByPlayerNameMap = WeakHashMap<Player, WeakReference<NPC>>()

    /**
     * Shows the HP Hud for the given [npc] to the [player].
     */
    fun showHud(player: Player, npc: NPC) {
        val currentNpc = npcByPlayerNameMap[player]?.get()
        if (currentNpc != npc) {
            player.sendDeveloperMessage("Switching HP Hud for $npc")
            npcByPlayerNameMap[player] = WeakReference(npc)
            player.hpHud.open(true, npc.id, npc.maxHitpoints)
            player.hpHud.updateValue(npc.hitpoints)
        }
    }

    /**
     * Removes the HP Hud for each player associated by the [npc] .
     */
    fun removeHud(npc: NPC) {
        val players = npcByPlayerNameMap.filterValues { it.refersTo(npc) }.keys
        players.forEach(PvmArenaNpcHealthBarHudManager::removeHud)
    }

    /**
     * Removes the HP Hud for the given [player].
     */
    fun removeHud(player: Player) {
        npcByPlayerNameMap.remove(player)
        player.hpHud.close()
    }

    /**
     * Updates the HP Huds for all players.
     */
    fun update() {
        val invalidKeys = npcByPlayerNameMap.filterValues { it.get() == null }.keys
        invalidKeys.forEach(npcByPlayerNameMap::remove)
        npcByPlayerNameMap.forEach { (player, boss) ->
            player.hpHud.updateValue(boss.get()?.hitpoints ?: 0)
        }
    }

    /**
     * Clears the HP Huds for all players.
     */
    fun clear() {
        npcByPlayerNameMap.keys.forEach { it.hpHud.close() }
        npcByPlayerNameMap.clear()
    }
}
