package com.zenyte.game.content.theatreofblood.room.xarpus.npc

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.utils.TimeUnit
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.function.Consumer

/**
 * @author Tommeh
 * @author Jire
 */
internal class XarpusExhumedPhase(
    xarpus: Xarpus,

    /**
     * The total amount of exhumeds that will be spawned during phase one.
     */
    private val maxExhumeds: Int,
    /**
     * The amount of health an exhumed will heal per tick if not stood on.
     */
    private val exhumedHealAmount: Int,
    /**
     * The amount of ticks to wait to spawn the next exhumed.
     */
    private val exhumedSpawnInterval: Int,
    /**
     * The amount of ticks an exhumed will stay in the fight for until it is removed.
     */
    private val exhumedLifespan: Int
) : XarpusPhase(xarpus) {

    /**
     * A list of currently active exhumeds.
     */
    private val exhumeds: MutableList<Pair<Int, WorldObject>> = ObjectArrayList()

    /**
     * The total amount of exhumeds spawned since the start of this fight.
     */
    private var exhumedIndex = 0

    override fun onPhaseStart() {
        xarpus.hitpoints = xarpus.maxHitpoints - exhumedHealAmount * exhumedLifespan * maxExhumeds
        xarpus.room.refreshHealthBar()
    }

    override fun onTick() {
        if (xarpus.ticks <= START_DELAY) return
        if (canSpawnExhumed())
            spawnExhumed()
        processActiveExhumeds()
    }

    private fun canSpawnExhumed() = xarpus.ticks % exhumedSpawnInterval == 0 && exhumedIndex < maxExhumeds

    private fun spawnExhumed() {
        val exhumed = WorldObject(
            ObjectId.EXHUMED, 22, 0,
            xarpus.location.random(6, 8, 6, 8, 0, 2, 0, 2)
        )
        val pair = Pair<Int, WorldObject>(xarpus.ticks, exhumed)
        exhumeds.add(pair)
        exhumedIndex++
        WorldTasksManager.schedule {
            World.sendObjectAnimation(exhumed, Animation(8065))
        }
        World.spawnTemporaryObject(exhumed, exhumedLifespan + 1) {
            exhumeds.remove(pair)
            World.sendGraphics(exhumedDespawnGfx, exhumed.position)
        }
    }

    private fun processActiveExhumeds() {
        exhumeds.forEach(Consumer { (first, second): Pair<Int, WorldObject> ->
            if (canExhumedHeal(first, second)) {
                WorldTasksManager.schedule(
                    {
                        xarpus.hitsplatHeal(exhumedHealAmount)
                        xarpus.healedAmount = xarpus.healedAmount + exhumedHealAmount
                    },
                    0.coerceAtLeast(
                        World.sendProjectile(
                            second,
                            xarpus,
                            exhumedHealProj
                        ) - 1
                    )
                )
            }
        })
    }

    private fun canExhumedHeal(startTick: Int, exhumedObject: WorldObject): Boolean {
        for (p in xarpus.room.validTargets) {
            if (p.location.positionHash == exhumedObject.positionHash) return false
        }
        return xarpus.ticks >= startTick + 3 // First three ticks are used for animation.
    }

    override val isPhaseComplete
        get() = xarpus.ticks >= maxExhumeds * exhumedSpawnInterval + exhumedLifespan + START_DELAY + END_DELAY

    override fun advance() = XarpusPoisonPhase(xarpus)

    companion object {

        /**
         * The amount of ticks before Xarpus starts spawning exhumed.
         */
        private val START_DELAY = TimeUnit.SECONDS.toTicks(4).toInt()

        /**
         * The amount of ticks before next phase starts after Xarpus finishes spawning exhumed.
         */
        private val END_DELAY = TimeUnit.SECONDS.toTicks(4).toInt()

        private val exhumedDespawnGfx = Graphics(1549)
        private val exhumedHealProj = Projectile(1550, 0, 80, 0, 60, 30, 0, 1)

    }

}