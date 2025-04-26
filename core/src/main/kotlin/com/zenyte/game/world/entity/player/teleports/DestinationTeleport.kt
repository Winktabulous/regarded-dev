package com.zenyte.game.world.entity.player.teleports

import com.near_reality.cache.interfaces.teleports.Destination
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.world.entity.Location

/**
 * @author Jire
 */
data class DestinationTeleport(
    val destination: Destination
) : Teleport {

    override fun getType() = TeleportType.NEAR_REALITY_PORTAL_TELEPORT
    override fun getDestination(): Location = destination.location.copy()
    override fun getLevel() = 0
    override fun getExperience() = 0.0
    override fun getRandomizationDistance() = 2
    override fun getRunes() = null
    override fun getWildernessLevel() = Teleport.WILDERNESS_LEVEL
    override fun isCombatRestricted() = false

}
