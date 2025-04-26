package com.near_reality.game.content.gauntlet.item.actions

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.Location

class GauntletCrystalTeleport(private val destination: Location) : Teleport {
    override fun getType(): TeleportType = TeleportType.REGULAR_TELEPORT
    override fun getDestination(): Location = destination
    override fun getLevel(): Int = 0
    override fun getExperience(): Double = 0.0
    override fun getRandomizationDistance(): Int = 0
    override fun getRunes(): Array<Item> = emptyArray()
    override fun getWildernessLevel(): Int = 30
    override fun isCombatRestricted(): Boolean = Teleport.UNRESTRICTED
}