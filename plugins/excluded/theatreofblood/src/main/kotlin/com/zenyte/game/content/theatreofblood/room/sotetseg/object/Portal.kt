package com.zenyte.game.content.theatreofblood.room.sotetseg.`object`

import com.zenyte.game.content.theatreofblood.room.sotetseg.ShadowRealmRoom
import com.zenyte.game.content.theatreofblood.room.sotetseg.npc.Sotetseg
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.RegionArea

/**
 * @author Tommeh
 * @author Jire
 */
internal class Portal : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (!option.equals("Enter", ignoreCase = true)) return
        val area: RegionArea = player.area as? ShadowRealmRoom ?: return
        val realm = area as ShadowRealmRoom
        if (realm.completed) return
        val sotetseg = realm.boss as Sotetseg
        realm.completed = true
        sotetseg.completeMaze(player)
    }

    override fun getObjects() = Portal.objects

    companion object {

        private val objects = arrayOf(ObjectId.PORTAL_33037)

    }

}