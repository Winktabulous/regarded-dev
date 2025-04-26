package com.near_reality.game.content.dt2.npc.whisperer

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.screech.ScreechSpecialAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.TentacleAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.impl.MeleeAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.WhispererSpecial
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.EnragedSpecialAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.ShadowLeachSpecialAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.siphon.SoulSiphonSpecialAttack
import com.near_reality.game.util.Ticker
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.THE_WHISPERER
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import kotlin.random.Random

//    val Sanity = PlayerVarbit(15064, false)
data class WhispererCombat(
    var spawnLocation: Location,
    private var primaryRotation: Boolean = Random.nextBoolean(),
    val state: WhispererState = WhispererState(),
    private var basicAttackDelay: Ticker = Ticker(10),
    private var meleeDelay: Ticker = Ticker(10)
) : NPC(
    THE_WHISPERER,
    spawnLocation,
    true
), CombatScript {

    fun getSpecialAttack(): WhispererSpecial {
        return when(state.phase) {
            WhispererPhase.First -> if (primaryRotation) ShadowLeachSpecialAttack else ScreechSpecialAttack
            WhispererPhase.Second -> if (primaryRotation) ScreechSpecialAttack else SoulSiphonSpecialAttack
            WhispererPhase.Third -> if (primaryRotation) SoulSiphonSpecialAttack else ShadowLeachSpecialAttack
            WhispererPhase.Fourth -> EnragedSpecialAttack
        }
    }

    private fun triggerSpecial(player: Player): Boolean {
        if (state.usingSpecial) return true
        val targetHp = (state.phase.specialHpPercentThreshold * maxHitpoints).toInt()
        val specialReady = hitpoints < targetHp
        state.usingSpecial = specialReady
        if (specialReady) {
            getSpecialAttack().setup(this,  player)
            getSpecialAttack().startTimer(this, player)
            getSpecialAttack().execute(this, player)
        }
        return state.usingSpecial
    }

    override fun attack(target: Entity): Int {
        if (triggerSpecial(target as Player)) return 1
        basicAttackDelay.tick()
        meleeDelay.tick()
        if (basicAttackDelay.finished) {
            executeBasicAttack(target)
            return 1
        }
        else if (meleeDelay.finished && this under target || this nextTo target) {
            MeleeAttack()(this, target)
            meleeDelay.reset()
        }
        return 1
    }

    fun executeBasicAttack(target: AbstractEntity) {
        val attacks = state.phase.generateAttacks() + TentacleAttack()
        val whisperer = this
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                when (ticks) {
                    0 -> {
                        whisperer seq 10245
                        attacks[ticks].invoke(whisperer, target)
                    }
                    1 -> {
                        whisperer seq 10246
                        attacks[ticks].invoke(whisperer, target)
                    }
                    2 -> {
                        whisperer seq 10245
                        attacks[ticks].invoke(whisperer, target)
                    }
                    3 -> {
                        whisperer seq 10246
                        attacks[ticks].invoke(whisperer, target)
                        stop()
                    }
                }
                ticks++
            }
        }, 0, 0)
        basicAttackDelay.reset()
    }

    override fun handleIngoingHit(hit: Hit?) {
        if (this.isLocked) return
        if (this.state.usingSpecial)
            if (hit != null)
                hit.damage = (hit.damage * 0.33).toInt()

        super.handleIngoingHit(hit)
    }

    override fun applyHit(target: Entity?, vararg hits: Hit?) {
        if (this.isLocked) return
        super<CombatScript>.applyHit(target, *hits)
    }

    override fun delayHit(delay: Int, target: Entity?, vararg hits: Hit?) {
        if (this.isLocked) return
        super.delayHit(delay, target, *hits)
    }

    override fun setRespawnTask() {}

}
