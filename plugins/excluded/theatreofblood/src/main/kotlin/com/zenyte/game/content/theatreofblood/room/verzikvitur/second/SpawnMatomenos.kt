package com.zenyte.game.content.theatreofblood.room.verzikvitur.second

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasMatomenos
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnMatomenos() {
    lastShield = WorldThread.WORLD_CYCLE
    animation = spawnMatomenosAnimation

    if (crabs.isNotEmpty()) {
        for (crab in crabs) {
            if (!crab.isDead && !crab.isFinished) {
                scheduleHit(crab, Hit(crab.hitpoints, HitType.HEALED), 0)
            }
            crab.finish()
            //TODO animation, projectile etc
        }
        crabs.clear()
    }

    crabs.add(NylocasMatomenos(room, room.getBaseLocation(38, 24)).apply(NPC::spawn))
    crabs.add(NylocasMatomenos(room, room.getBaseLocation(26, 24)).apply(NPC::spawn))
}

private val spawnMatomenosAnimation = Animation(8117)