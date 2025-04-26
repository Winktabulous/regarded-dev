package com.zenyte.game.content.boss.abyssalsire.spawns

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireSpawn(
    location: Location,
    private val spawns: AbyssalSireSpawns,
    private val sire: AbyssalSire
) : NPC(NpcId.SPAWN, location, Direction.SOUTH, 5), CombatScript {

    override fun spawn(): NPC {
        spawned = true
        attackDistance = 4
        setTarget(sire.target!!.get())
        WorldTasksManager.schedule({
            if (isFinished || isDead) return@schedule

            sire.scionMatured = true
            setTransformation(NpcId.SCION)
        }, 19)

        return super.spawn()
    }

    override fun finish() {
        super.finish()

        spawns.removeSpawn(this)
    }

    override fun attack(target: Entity): Int {
        animate()
        if (!combat.outOfRange(target, 0, target!!.size, true)) {
            delayHit(0, target, melee(target, if (id == NpcId.SCION) 15 else 6))
        } else {
            delayHit(World.sendProjectile(this, target, rangedProj), target, ranged(target, if (id == NpcId.SCION) 15 else 6))
        }

        return getCombatDefinitions().attackSpeed
    }

    companion object {
        private val rangedProj = Projectile(628, 42, 30, 40, 15, 10, 64, 5)
    }

}