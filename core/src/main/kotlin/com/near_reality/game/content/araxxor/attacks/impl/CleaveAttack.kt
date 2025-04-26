package com.near_reality.game.content.araxxor.attacks.impl

import com.near_reality.game.content.*
import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.attacks.Attack
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class CleaveAttack: Attack {
    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null) return
        araxxor.forceTalk = ForceTalk("Skree!")
        araxxor.lock()
        araxxor seq 11483
        val centerTile = target.location.copy()
        schedule(1) { stomp(araxxor, target, centerTile) }
    }

    private fun stomp(araxxor: Araxxor, target: Entity, centerTile: Location) {
        var maxHit = 38
        if ((target as Player).prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
            maxHit = (maxHit * 0.20).toInt()
        val damage = CombatUtilities.getRandomMaxHit(araxxor, maxHit, AttackType.CRUSH, target)
        if (target.location == centerTile)
            target.scheduleHit(araxxor, araxxor hit target withVenom damage, 0)
        // Add acid pools on the player's location, and on either side of the player
        araxxor.instance.spawnAcidPool(centerTile)
        when(araxxor.direction) {
            North.direction, South.direction -> {
                araxxor.instance.spawnAcidPool(centerTile offset Pair(-1, 0))
                araxxor.instance.spawnAcidPool(centerTile offset Pair(1, 0))
            }
            East.direction, West.direction -> {
                araxxor.instance.spawnAcidPool(centerTile offset Pair(0, -1))
                araxxor.instance.spawnAcidPool(centerTile offset Pair(0, 1))
            }
        }
        araxxor.unlock()
    }
}