package com.near_reality.game.content.seasonal.easter_2024

import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.PolygonRegionArea
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin

/**
 * Marks the region in which the Colossal Chocco Chicken boss spawns.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class Easter2024EventArea :
    PolygonRegionArea(),
    CannonRestrictionPlugin
{
    override fun enter(player: Player?) {
        player?.sendDeveloperMessage("You have entered the Easter 2024 event area.")
        player?.viewDistance = Player.SCENE_DIAMETER
        Easter2024Manager.chickenOrNull?.run {
            player?.hpHud?.open(true, id, maxHitpoints)
        }
    }

    override fun leave(player: Player?, logout: Boolean) {
        player?.sendDeveloperMessage("You have left the Easter 2024 event area.")
        player?.resetViewDistance()
        player?.hpHud?.close()
    }

    override fun name(): String = "Easter 2024 Event Area"

    override fun inside(location: Location?): Boolean = location?.regionId == 7504

    override fun polygons() = arrayOf(RSPolygon(7504))
}
