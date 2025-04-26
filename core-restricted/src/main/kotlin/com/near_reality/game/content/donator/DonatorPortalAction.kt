package com.near_reality.game.content.donator

import com.near_reality.game.content.wilderness.event.WildernessEvent
import com.near_reality.game.content.wilderness.event.WildernessEventManager
import com.near_reality.game.content.wilderness.event.ganodermic_beast.GanodermicBeastEvent
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler
import com.zenyte.game.content.wildernessVault.WildernessVaultStatus
import com.zenyte.game.item.Item
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

@Suppress("unused")
class DonatorPortalAction : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (option.equals("Wilderness-Vault", ignoreCase = true)) {
            val instance = WildernessVaultHandler.getInstance()
            if (instance.vaultStatus == WildernessVaultStatus.INACTIVE) {
                player.dialogue { plain("The wilderness vault is currently inactive, come back later.") }
            } else {
                val loc = instance.currentSpawn.locationPlayer()
                val teleport = teleport(loc)
                warnTele(player, `object`, teleport)
            }
        } else if (option.equals("Ganodermic-Beast", ignoreCase = true)) {
            when(val state = WildernessEventManager.stateOf(GanodermicBeastEvent)) {
                is WildernessEvent.State.Active -> {
                    val teleportLocation = GanodermicBeastEvent.teleportLocation().orElse(null)
                    if (teleportLocation == null)
                        player.dialogue { plain("Oop! Something went wrong, please make a ticket on discord!") }
                    else {
                        val teleport = teleport(teleportLocation)
                        warnTele(player, `object`, teleport)
                    }
                }
                is WildernessEvent.State.Scheduled -> player.dialogue { plain(state.toString()) }
                else -> player.dialogue { plain("The Ganodermic Beast is not currently active.") }
            }
        }
    }

    override fun getObjects(): Array<Any> =
        arrayOf(OBELISK)

    private fun warnTele(player: Player, worldObject: WorldObject, teleport: Teleport) {
        player.dialogue {
            plain("You're about to teleport to the Wilderness!")
            options("Are you sure you want to teleport", "Yes.", "No.").onOptionOne {
                doTeleport(
                    player,
                    worldObject,
                    teleport
                )
            }
        }
    }

    private fun doTeleport(player: Player?, worldObject: WorldObject, teleport: Teleport) {
        schedule(object : WorldTask {
            var ticks: Int = 0

            override fun run() {
                when (ticks) {
                    0 -> World.spawnObject(WorldObject(OBELISK_2, 10, 1, worldObject.location.copy()))
                    2 -> teleport.teleport(player)
                    3 -> {
                        World.spawnObject(WorldObject(OBELISK, 10, 1, worldObject.location.copy()))
                        stop()
                    }
                }
                ticks++
            }
        }, 0, 0)
    }

    private fun teleport(location: Location): Teleport {
        return object : Teleport {
            override fun getType(): TeleportType = TeleportType.WILDERNESS
            override fun getDestination(): Location = location
            override fun getLevel(): Int = 0
            override fun getExperience(): Double = 0.0
            override fun getRandomizationDistance(): Int = 0
            override fun getRunes(): Array<Item> = emptyArray()
            override fun getWildernessLevel(): Int = 0
            override fun isCombatRestricted(): Boolean = true
            override fun onUsage(player: Player) {}
        }
    }

    companion object {
        const val OBELISK: Int = 50101
        const val OBELISK_2: Int = 50102
    }
}
