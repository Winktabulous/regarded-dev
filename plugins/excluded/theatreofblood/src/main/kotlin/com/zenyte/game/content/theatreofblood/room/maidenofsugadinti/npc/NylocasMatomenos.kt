package com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.npc

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiRoom
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NpcId

/**
 * @author Tommeh
 * @author Jire
 */
internal class NylocasMatomenos(private val maiden: MaidenOfSugadinti, tile: Location) :
    TheatreNPC<MaidenOfSugadintiRoom>(
        maiden.room,
        NpcId.NYLOCAS_MATOMENOS,
        maiden.room.getLocation(tile)
    ) {

    // TODO freeze chances based on magic bonus

    private var dying = false

    init {
        setFaceEntity(maiden)
        isForceMultiArea = true
    }

    override fun processNPC() {
        if (dying || isFinished || isDead || room.completed) return
        if (!hasWalkSteps() && !isFrozen && Utils.getDistance(x, y, maiden.middleLocation.x, maiden.middleLocation.y) < 6) {
            if (!dying) absorb()
            return
        }
        if (!isFrozen && !hasWalkSteps())
            addWalkSteps(maiden.location.x, maiden.location.y)
    }

    override fun sendDeath() {
        dying = true
        setAnimation(Animation(8097))
        WorldTasksManager.schedule({
            onFinish(null)
        }, 1)
    }

    private fun absorb() {
        maiden.absorbNylocas(this)
        setHitpoints(0)
    }

    override fun autoRetaliate(source: Entity) {}

    override fun isDying() = dying

}