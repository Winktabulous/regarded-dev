package com.near_reality.game.content.wilderness.event.ganodermic_beast

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.TeleblockEffect
import com.zenyte.game.world.entity.player.container.RequestResult


private val MELEE_ANIMATION = Animation(30001)
private val RANGE_ANIMATION = Animation(30002)
private val MAGIC_ANIMATION = Animation(30002)

/**
 * Handles the combat behaviour of the [GanodermicBeast].
 *
 * @author Stan van der Bend
 */
sealed class GanodermicBeastAttack(val beast: GanodermicBeast) : CombatScript {

    abstract val range: Int
    abstract val damageType: HitType
    abstract val attackType: AttackType
    abstract val attackAnimation: Animation

    open val attackSpeed: Int = 6
    open val baseHitDelay: Int = 0
    open val projectile: Projectile? = null
    open val hitGraphic: Graphics? = null
    open val maxHit: Int
        get() = when (beast.phase) {
            GanodermicBeast.Phase.PHASE_1 -> 20
            GanodermicBeast.Phase.PHASE_2 -> 22
            GanodermicBeast.Phase.PHASE_3 -> 22
            GanodermicBeast.Phase.PHASE_4 -> 28
            GanodermicBeast.Phase.PHASE_5 -> 34
        }

    override fun attack(target: Entity?): Int {

        beast.animation = attackAnimation

        doAttack()

        return attackSpeed
    }

    protected open fun doAttack() {
        beast.findPlayerTargetsInRange(range)
            .forEach {
                var delayHitBy = baseHitDelay
                if (projectile != null)
                    delayHitBy += World.sendProjectile(beast, it, projectile)
                createHit(delayHitBy, it)
                hitGraphic?.run {
                    WorldTasksManager.scheduleOrExecute({
                        it.graphics = this
                    }, delayHitBy)
                }
            }
    }

    protected fun createHit(delayHitBy: Int, it: Player) {
        CombatUtilities.delayHit(
            beast, delayHitBy, it,
            Hit(beast, CombatUtilities.getRandomMaxHit(beast, maxHit, attackType, it), damageType, 0)
        )
    }


    class Melee(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val range: Int = 2
        override val damageType: HitType = HitType.MELEE
        override val attackType: AttackType = AttackType.CRUSH
        override val attackAnimation: Animation = MELEE_ANIMATION
    }

    class Magic(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val range: Int = 15
        override val damageType: HitType = HitType.MAGIC
        override val attackType: AttackType = AttackType.MAGIC
        override val attackAnimation: Animation = MAGIC_ANIMATION
        override val projectile: Projectile
            get() = Projectile(676, 40, 25, 25, 15, 18, 64, 5)
        override val hitGraphic: Graphics
            get() = Graphics(677, 25, 94)
    }

    class LightningTornado(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val range: Int = 15
        override val damageType: HitType = HitType.MAGIC
        override val attackType: AttackType = AttackType.MAGIC
        override val attackAnimation: Animation = MAGIC_ANIMATION
        override val projectile: Projectile
            get() = Projectile(1198, 5, 15, 20, 0, 25, 64, 2)
        override val hitGraphic: Graphics
            get() = Graphics(1196, 20, 0)
    }

    class Teleblock(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val attackAnimation: Animation = MAGIC_ANIMATION
        override val projectile: Projectile
            get() =  Projectile(1300, 43, 31, 46, 23, 29, 64, 5)
        override val damageType: HitType = HitType.MAGIC
        override val attackType: AttackType = AttackType.MAGIC
        override val range: Int = 15
        override val hitGraphic: Graphics
            get() = Graphics(345)

        override fun doAttack() {
            val plrs = beast.findPlayerTargetsInRange(range)
            if (plrs != null && plrs.isNotEmpty()) {
                val target: Player = Utils.random(plrs)
                val delay: Int = World.sendProjectile(beast, target, projectile)
                WorldTasksManager.schedule({
                    target.graphics = hitGraphic
                    TeleblockEffect().spellEffect(null, target, 1)
                }, delay)
            }
            super.doAttack()
        }
    }

    class LightningTiles(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val range: Int = 15
        override val damageType: HitType = HitType.RANGED
        override val attackType: AttackType = AttackType.RANGED
        override val attackAnimation: Animation = Animation(30003)

        override fun doAttack() {

            beast.freeze(10)

            val directions = arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
            for (direction in directions)
                sendLightning(beast.middleLocation.transform(direction, Utils.random(2, 3)))

            WorldTasksManager.schedule({
                if (!beast.isDead) {
                    beast.animation = attackAnimation
                    for (direction in directions) {
                        sendLightning(
                            beast.middleLocation
                                .transform(direction, Utils.random(2, 3))
                                .transform(Direction.values().random())
                        )
                    }
                }
            }, 4)
        }

        private fun sendLightning(base: Location) {
            val projectile =
                Projectile(1707, 70, 5, 25, 15, 18, 64, 5)
            WorldTasksManager.schedule({
                if (!beast.isDead) {
                    World.sendGraphics(Graphics(1913), base)
                    beast.lightningTiles += GanodermicBeast.LightningTile(15 + Utils.random(3) * 5, base)
                }
            }, World.sendProjectile(beast, base, projectile))
        }
    }

    class ShadowArrow(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val range: Int = 15
        override val damageType: HitType = HitType.RANGED
        override val attackType: AttackType = AttackType.RANGED
        override val attackAnimation: Animation = RANGE_ANIMATION
        override val projectile: Projectile
            get() = Projectile(597, 30, 35, 25, 10, 14, 64, 1)
    }

    class ShadowSpikes(beast: GanodermicBeast) : GanodermicBeastAttack(beast) {
        override val range: Int = 15
        override val damageType: HitType = HitType.MAGIC
        override val attackType: AttackType = AttackType.MAGIC
        override val attackAnimation: Animation = Animation(30003)

        override fun doAttack() {
            val proj =
                Projectile(380, 70, 5, 35, 30, 40, 64, 5)
            beast.findPlayerTargetsInRange(range).map {
                it to World.sendProjectile(beast, it, proj)
            }.groupBy { it.second }.forEach { (delay, playerList) ->
                playerList.forEach { (player, _) -> createHit(delay, player) }
                WorldTasksManager.schedule({
                    if (!beast.isDead) {
                        playerList.forEach { (player, _) ->
                            if (Utils.randomBoolean(5)) {
                                val weapon = player.weapon
                                if (weapon != null && player.inventory.hasSpaceFor(weapon)) {
                                    if (player.equipment.deleteItem(weapon).result == RequestResult.SUCCESS)
                                        player.inventory.addItem(weapon)
                                    player.sendMessage(Colour.GREY.wrap("A shadowy creature disarmed you!"))
                                }
                            }
                            World.sendGraphics(Graphics(379), player.location)
                        }
                    }
                }, delay)
            }
        }
    }
}
