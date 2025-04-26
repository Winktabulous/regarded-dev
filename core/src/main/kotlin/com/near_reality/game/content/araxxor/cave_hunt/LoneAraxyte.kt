package com.near_reality.game.content.araxxor.cave_hunt

import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId.ARAXYTE_LV_96
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-15
 */
class LoneAraxyte(val instance: AraxyteCaveHunt, spawnLocation: Location): Araxyte(ARAXYTE_LV_96, spawnLocation) {

    override fun attack(target: Entity?): Int {
        if (target == null) return 1
        this seq 11497
        val damage = CombatUtilities.getRandomMaxHit(this, 15, AttackType.MELEE, target)
        target.scheduleHit(this, this hit target damage damage, 0)
        return 6
    }

    override fun sendDeath() {
        instance.roomCompleted = true
        val source = mostDamagePlayerCheckIronman
        val spawnDefinitions = combatDefinitions.spawnDefinitions
        setAnimation(spawnDefinitions.deathAnimation)
        optionMask = 0
        val sound = spawnDefinitions.deathSound
        if (sound != null && source != null)
            source.sendSound(sound)
        remove()
    }
    override fun hatchEgg(target: Entity?) {}
}