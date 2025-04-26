package com.near_reality.game.content.seasonal.easter_2024

import com.google.common.eventbus.Subscribe
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.events.ServerLaunchEvent

/**
 * Easter 2024 module for registering global hooks.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object Easter2024Module {

    @Subscribe
    @JvmStatic
    fun onServerLaunch(event: ServerLaunchEvent) {
        Easter2024Manager.spawnJaseNpc()
        Easter2024Manager.changeState(Easter2024State.WAITING)
        registerCommands()
    }

    private fun registerCommands() {
        Command(PlayerPrivilege.DEVELOPER, "chicken") { player, _ ->
            player.dialogue {
                options {
                    "Spawn" {
                        Easter2024Manager.changeState(Easter2024State.CHICKEN_SPAWNING)
                    }
                    "Items" {
                        player.inventory.clear()
                        player.inventory.addItem(Item(ItemId.BUCKET_OF_WATER, 5))
                        player.inventory.addItem(Item(ItemId.BUCKET_OF_SAND, 5))
                        player.inventory.addItem(Item(ItemId.BUCKET, 5))
                        player.inventory.addItem(Item(ItemId.TINDERBOX, 1))
                        player.inventory.addItem(Item(ItemId.LOGS, 12))
                    }
                    "Next" {
                        val chicken = Easter2024Manager.chickenOrNull
                        if (chicken != null) {
                            val newHitpoints = chicken.type.switchOnDamagePercentage * chicken.maxHitpoints
                            chicken.hitpoints = newHitpoints.toInt()
                        }
                    }
                    (if(Easter2024Manager.enabled) "Disable" else "Enable") {
                        Easter2024Manager.enabled = !Easter2024Manager.enabled
                        if (Easter2024Manager.enabled) {
                            Easter2024Manager.changeState(Easter2024State.WAITING)
                        }
                        player.sendMessage("Easter 2024 event is now ${if(Easter2024Manager.enabled) "enabled" else "disabled"}.")
                    }
                    "Set Max Hp" {
                        player.sendInputInt("Enter the new max hitpoints") { newMaxHp ->
                            ColossalChoccoChicken.configuredMaxHitpoints = newMaxHp
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.PLAYER, "easter") { player, _ ->
            if (!player.isLocked) {
                val teleport: Teleport = object : Teleport {
                    override fun getType(): TeleportType = TeleportType.CROP_CIRCLE
                    override fun getDestination(): Location = Location(1902, 5143, 2)
                    override fun getLevel(): Int = 0
                    override fun getExperience(): Double = 0.0
                    override fun getRandomizationDistance(): Int = 3
                    override fun getRunes(): Array<Item> = emptyArray()
                    override fun getWildernessLevel(): Int = 0
                    override fun isCombatRestricted(): Boolean = false
                }
                teleport.teleport(player)
            }
        }
    }
}
