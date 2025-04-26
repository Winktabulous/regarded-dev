@file:Suppress("unused")

package com.near_reality.game.content.pvm_arena.area

import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcHealthBarHudManager
import com.near_reality.game.content.pvm_arena.PvmArenaManager
import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.near_reality.game.content.pvm_arena.player.revive.PvmArenaDownedPlayerWorldTask
import com.near_reality.game.content.pvm_arena.player.revive.PvmArenaReviveState
import com.zenyte.game.item.Item
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.region.PolygonRegionArea
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.plugins.*

object PvmArenaBlueFightArea : PvmArenaFightArea(
    id = 1,
    polygon = RSPolygon(1697, 4690, 1722, 4715),
)

object PvmArenaRedFightArea : PvmArenaFightArea(
    id = 2,
    polygon = RSPolygon(1671, 4690, 1695, 4715),
)

/**
 * Represents one of two fighting areas where teams are teleported to once the PvM Arena activity starts.
 *
 * @author Stan van der Bend
 */
sealed class PvmArenaFightArea(
    private val id: Int,
    internal val polygon: RSPolygon,
) :
    PolygonRegionArea(),
    CannonRestrictionPlugin,
    LoginPlugin,
    EquipmentPlugin,
    EntityAttackPlugin,
    DeathPlugin
{
    /**
     * TODO: Get random position inside a smaller area sub section of the polygon.
     */
    fun randomSpawnLocation(): Location =
        polygon.getRandomPosition(0, 5)

    override fun login(player: Player?) {
        player?.let(PvmArenaLobbyArea.Companion::moveInto)
    }

    override fun enter(player: Player?) {
        player?.let(PvmArenaManager::onEnterFightArea)
    }

    override fun leave(player: Player?, logout: Boolean) {
        player?.let(PvmArenaManager::onLeaveFightArea)
    }

    override fun name(): String =
        "PvM Arena $id"

    override fun polygons(): Array<RSPolygon> =
        arrayOf(polygon)

    override fun safeHardcoreIronmanDeath(): Boolean =
        true

    override fun isSafe(): Boolean =
        true

    override fun getDeathInformation(): String =
        "You have died in the PvM Arena. You will not lose any items."

    override fun getRespawnLocation(): Location =
        Location(1761, 4701, 0)

    override fun isMultiwayArea(position: Position?): Boolean =
        true

    override fun attack(player: Player?, entity: Entity?, combat: PlayerCombat?): Boolean {
        player?:return super.attack(null, entity, combat)
        if (PvmArenaReviveState.isDownOrCancelActionAndFalse(player, "You are down and cannot attack."))
            return false
        else {
            val target = (entity as? NPC) ?: return true
            PvmArenaNpcHealthBarHudManager.showHud(player, target)
            return true
        }
    }

    override fun startAttack(player: Player?, entity: Entity?): Boolean {
        val starting = super.startAttack(null, entity)
        if (starting && player != null && entity is NPC)
            PvmArenaNpcHealthBarHudManager.showHud(player, entity)
        return true
    }

    override fun equip(player: Player?, item: Item?, slot: Int): Boolean {
        player?:return super.equip(null, item, slot)
        return !PvmArenaReviveState.isDownOrCancelActionAndFalse(player, "You are down and cannot equip items.")
    }

    override fun unequip(player: Player?, item: Item?, slot: Int): Boolean {
        player?:return super.unequip(null, item, slot)
        return !PvmArenaReviveState.isDownOrCancelActionAndFalse(player, "You are down and cannot unequip items.")
    }

    override fun sendDeath(player: Player?, source: Entity?): Boolean {
        player?:return false
        val team = PvmArenaTeam.findTeamContaining(player)?:return false
        when {
            team.players.size == 1 -> return false
            PvmArenaReviveState.isDownOrCancelActionAndFalse(player) -> return false
            else -> {
                PvmArenaDownedPlayerWorldTask.schedule(player)
                return true
            }
        }
    }

    companion object {
        fun findAreaForLocation(location: Location) = when {
            PvmArenaBlueFightArea.polygon.contains(location) -> PvmArenaBlueFightArea
            PvmArenaRedFightArea.polygon.contains(location) -> PvmArenaRedFightArea
            else -> null
        }
        fun inAnyFightArea(location: Location) =
            findAreaForLocation(location) != null
    }
}
