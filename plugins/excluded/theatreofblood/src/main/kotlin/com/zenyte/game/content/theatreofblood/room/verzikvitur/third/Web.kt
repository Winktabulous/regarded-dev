package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
internal class Web(room: VerzikViturRoom, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, ID, location), CombatScript {

    val worldObject = WorldObject(32734, tile = location)

    override fun attack(target: Entity?) = 0

    override fun applyHit(hit: Hit?) {
        super<TheatreNPC>.applyHit(hit)
        if(!this.isFinished) {
            for (p in room.validTargets) {
                if (location == p.location) {
                    p.graphics = Graphics(1600)
                    WorldTasksManager.schedule {
                        p.resetFreeze()
                        p.sendMessage("The web crumbles under your attack and you are released safely.")
                    }
                }
            }
            finish()
        }
    }

    override fun finish() {
        super.finish()

        room.verzikVitur.webLocs.remove(location)
        World.removeObject(worldObject)
    }

    override fun spawn(): NPC = super.spawn().apply {
        World.spawnObject(worldObject)

        WorldTasksManager.schedule({
            if(this.isDead || this.isFinished)
                return@schedule
            for (p in room.validTargets) {
                if (location == p.location) {
                    p.graphics = Graphics(1600)
                    WorldTasksManager.schedule {
                        p.resetFreeze()
                        p.sendMessage("The web explodes and you are released.")
                        p.applyHit(Hit(this, Utils.random(40), HitType.REGULAR))
                    }
                }
            }

            finish()
        }, 12)
    }

    companion object {
        const val ID = 8376
    }

}