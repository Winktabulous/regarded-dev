package com.near_reality.game.content.araxxor.objects

import com.near_reality.game.content.araxxor.AraxxorInstance
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId.WEB_TUNNEL_ARAXXOR
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-23
 */
class AraxxorTunnelEntrance : ObjectAction {
    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return
        `object` ?: return
        AraxxorInstance(player).constructRegion()
    }

    override fun getObjects(): Array<Any> = arrayOf(WEB_TUNNEL_ARAXXOR)
}