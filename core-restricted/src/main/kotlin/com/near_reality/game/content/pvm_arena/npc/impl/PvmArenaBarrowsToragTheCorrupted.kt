package com.near_reality.game.content.pvm_arena.npc.impl

import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcHealthBarHudManager
import com.near_reality.game.content.pvm_arena.PvmArenaManager
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpc
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcHealthBar
import com.near_reality.game.content.pvm_arena.player.revive.pvmArenaReviveState
import com.zenyte.game.content.minigame.barrows.wights.*
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.SkipPluginScan

/**
 * Represents the Torag the corrupted npc in the PvM Arena activity.
 *
 * @author Stan van der Bend
 */
@SkipPluginScan
internal class PvmArenaBarrowsToragTheCorrupted(override val config: PvmArenaNpc.SpawnConfig):
    ToragTheCorrupted(NpcId.TORAG_THE_CORRUPTED, config.team.area.randomSpawnLocation(), Direction.SOUTH, 2),
    PvmArenaNpc
{
    init {
        hitBar = PvmArenaNpcHealthBar(this)
        aggressionDistance = 15
        pvmArenaVersion = true
    }

    override fun spawn(): NPC =
        super.spawn().also { hitpoints = maxHitpoints }

    override fun getMaxHitpoints(): Int =
        config.transformMaxHitPoints(super.getMaxHitpoints())

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        PvmArenaManager.onNpcDeath(this)
    }

    override fun isPotentialTarget(entity: Entity?): Boolean =
        if (entity is Player && (!entity.pvmArenaReviveState.canBeAttacked || !config.team.containsPlayer(entity)))
            false
        else
            super.isPotentialTarget(entity)

    override fun handleOutgoingHit(target: Entity?, hit: Hit?) {
        if (target is Player && !target.pvmArenaReviveState.canBeAttacked)
            return
        super.handleOutgoingHit(target, hit)
    }

    override fun postHitProcess(hit: Hit?) {
        super.postHitProcess(hit)
        PvmArenaNpcHealthBarHudManager.update()
    }

    override fun sendDeath() {
        super.sendDeath()
        PvmArenaNpcHealthBarHudManager.removeHud(this)
    }

    override fun setRespawnTask() {
        // No respawning for PvM Arena npcs.
    }

    override fun drop(tile: Location?) {
        // No drops for PvM Arena npcs.
    }

    override fun isEntityClipped(): Boolean =
        false

    override fun isInWilderness(): Boolean =
        true // Do this so the wilderness weapon buffs are applied

    override fun getCombatLevel(): Int =
        250
}
