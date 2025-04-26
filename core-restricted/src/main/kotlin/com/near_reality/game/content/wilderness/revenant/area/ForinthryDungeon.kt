package com.near_reality.game.content.wilderness.revenant.area

import com.near_reality.game.content.wilderness.revenant.npc.RevenantMaledictus
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.wilderness.WildernessArea

/**
 * @author Kris | 31/01/2019 03:43
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
open class ForinthryDungeon : WildernessArea() {

    override fun name(): String = "Forinthry Dungeon"

    public override fun polygons(): Array<RSPolygon> = arrayOf(
        RSPolygon(
            arrayOf(
                intArrayOf(3136, 10247),
                intArrayOf(3136, 10036),
                intArrayOf(3270, 10037),
                intArrayOf(3271, 10249)
            )
        )
    )

    override fun enter(player: Player) {
        super.enter(player)
        player.teleportManager.unlock(PortalTeleport.FORINTHRY_DUNGEON)
        RevenantMaledictus.instance
            ?.takeUnless { it.isDead || it.isFinished }
            ?.let { player.sendMessage(it.spawn.localHintMessage(true)) }
    }
}
