package com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.combat.CombatScript

/**
 * @author Jire
 */
internal class NylocasMatomenos(room: VerzikViturRoom, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, ID, location), CombatScript {

    override fun attack(target: Entity?) = 0

    override fun autoRetaliate(source: Entity?) {}

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)

        if (hit.hitType == HitType.SHIELD) {
            hit.damage = 0
        }
    }

    companion object {
        const val ID = 8385
    }

}