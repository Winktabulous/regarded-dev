package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.siphon

import com.near_reality.game.content.damage
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.hit
import com.near_reality.game.content.offset
import com.near_reality.game.content.sanity
import com.zenyte.game.content.consumables.Consumable
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.LOST_SOUL
import com.zenyte.game.world.entity.npc.NpcId.LOST_SOUL_12212
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-27
 */
class LostSoul(
    val whisperer: WhispererCombat,
    inShadowRealm: Boolean = false,
    val soulType: LostSoulType,
    soulOffset: Pair<Int, Int>,
) : NPC(
    if (inShadowRealm) LOST_SOUL_12212 else LOST_SOUL,
    whisperer.middleLocation offset soulOffset,
    true
) {

    override fun onDeath(source: Entity?) {
        if (source is Player) {
            when(soulType) {
                // green souls: Deals 50 damage to the Whisperer
                LostSoulType.MORS -> whisperer.applyHit(this hit whisperer damage 50)
                // blue souls: Restores ~20% prayer points
                LostSoulType.ORATIO -> Consumable.Restoration(SkillConstants.PRAYER, 0.2f, 7).apply(source)
                // cyan souls: Restores 15% sanity
                LostSoulType.SANITAS -> source sanity 15
                // yellow souls: Restores ~20% hp.
                LostSoulType.VITA -> Consumable.Restoration(SkillConstants.HITPOINTS, 0.2f, 7).apply(source)
            }
            SoulSiphonSpecialAttack.lostSouls.remove(this)

            if (SoulSiphonSpecialAttack.lostSouls.isEmpty()) {
                whisperer.state.transitionPhase()
                whisperer.state.usingSpecial = false
                // If all lost souls are killed, 75 damage is dealt instead,
                whisperer.applyHit(this hit whisperer damage 25)
            }
        }
    }

    init {
        radius = 0
        faceLocation = whisperer.middleLocation
    }

    override fun processNPC() {
        super.processNPC()
        forceTalk = ForceTalk(soulType.chant)
    }

    // No respawning
    override fun setRespawnTask() {}

}