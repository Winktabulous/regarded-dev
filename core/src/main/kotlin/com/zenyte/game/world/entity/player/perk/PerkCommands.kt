package com.zenyte.game.world.entity.player.perk

import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.world.entity.player.exchangePoints
import com.zenyte.game.content.boons.BoonWrapper
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import kotlin.jvm.optionals.getOrNull

object PerkCommands {
    fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "takeperk") { player, _ ->
            player.sendInputName("Enter target player") { botName ->
                val targetPlayer = World.getPlayer(botName).getOrNull()
                if (targetPlayer == null)
                    player.dialogue { plain("No player online found by name `$botName`") }
                else {
                    player.sendInputInt("Enter a perk id:"){
                        val perk = BoonWrapper.PERKS_BY_ID.getOrDefault(it, BoonWrapper.Unknown)
                        if(perk != BoonWrapper.Unknown) {
                            targetPlayer.boonManager.unlockedBoons.remove(perk.perk.javaClass.simpleName)
                            player.sendMessage("Removed unlocked boon: ${perk.perk.javaClass.simpleName} from ${targetPlayer.username}")
                        } else {
                            player.sendMessage("Unknown perk id entered. Please use the list present in the management discord")
                        }
                    }
                }
            }
        }

        GameCommands.Command(PlayerPrivilege.DEVELOPER, "giveperk") { player, _ ->
            player.sendInputName("Enter target player") { botName ->
                val targetPlayer = World.getPlayer(botName).getOrNull()
                if (targetPlayer == null)
                    player.dialogue { plain("No player online found by name `$botName`") }
                else {
                    player.sendInputInt("Enter a perk id:"){
                        val perk = BoonWrapper.PERKS_BY_ID.getOrDefault(it, BoonWrapper.Unknown)
                        if(perk != BoonWrapper.Unknown) {
                            targetPlayer.boonManager.unlockedBoons.add(perk.perk.javaClass.simpleName)
                            player.sendMessage("Added unlocked boon: ${perk.perk.javaClass.simpleName} to ${targetPlayer.username}")
                        } else {
                            player.sendMessage("Unknown perk id entered. Please use the list present in the management discord")
                        }
                    }
                }
            }
        }

        GameCommands.Command(PlayerPrivilege.DEVELOPER, "setremnantpoints") { player, _ ->
            player.sendInputName("Enter target player") { botName ->
                val targetPlayer = World.getPlayer(botName).getOrNull()
                if (targetPlayer == null)
                    player.dialogue { plain("No player online found by name `$botName`") }
                else {
                    player.sendInputInt("Enter points to set to: (current: ${ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, player)})"){
                        player.exchangePoints = it
                        player.sendMessage("Set points of player ${targetPlayer.username} to $it")
                    }
                }
            }
        }
    }
}