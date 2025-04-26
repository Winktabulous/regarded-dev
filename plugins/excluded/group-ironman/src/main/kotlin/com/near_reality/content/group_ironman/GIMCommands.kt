package com.near_reality.content.group_ironman

import com.near_reality.content.group_ironman.player.ironmanGroupType
import com.near_reality.content.group_ironman.player.leftTheNode
import com.near_reality.game.world.entity.player.UsernameProvider
import com.zenyte.game.content.skills.magic.spells.teleports.RegularTeleport
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.region.area.EdgevilleArea
import com.zenyte.game.world.region.area.wilderness.WildernessArea

object GIMCommands {

    fun register() {
        Command(PlayerPrivilege.ADMINISTRATOR, "managegim") { p, _ ->
            p.sendInputString("What player would you like to manage") {playerName ->
                val player = World.getPlayer(playerName)
                if(player.isPresent) {
                    if(player.get().gameMode.isGroupIronman) {
                        p.dialogue {
                            options("GIM Management: ${player.get().name}") {
                                dialogueOption("Add to Group", noPlayerMessage = true) {
                                    p.sendInputString("Which player's group should this player be added to") { groupPlayerName ->
                                        val groupPlayer = World.getPlayer(groupPlayerName)
                                        if (groupPlayer.isPresent) {
                                            val group = IronmanGroup.find(groupPlayer.get())
                                            if (group == null)
                                                p.sendMessage("Could not find group for target player")
                                            else {
                                                if (group.activeMembers.size >= IronmanGroup.MAX_GROUP_SIZE) {
                                                    p.sendMessage("Active members of target group are at maximum size")
                                                } else {
                                                    group.join(player.get(), true)
                                                    p.sendMessage("${player.get().name} has been added to ${groupPlayer.get().name}'s GIM group.")
                                                    player.get().sendMessage("You have been added to ${groupPlayer.get().name}'s GIM group.")
                                                }
                                            }
                                        } else {
                                            p.sendMessage("Could not find player by that name")
                                        }
                                    }
                                }
                                dialogueOption("Remove from Group (no cooldown)") {
                                    val group = IronmanGroup.find(player.get())
                                    if(group == null) {
                                        p.sendMessage("Could not find group for target player")
                                    } else {
                                        group.remove(player.get())
                                        player.get().sendMessage("You have been removed from your GIM group.")
                                        p.sendMessage("${player.get().name} has been removed from their GIM group.")
                                    }
                                }

                                dialogueOption("Promote to Leader") {
                                    val group = IronmanGroup.find(player.get())
                                    if(group == null) {
                                        p.sendMessage("Could not find group for target player")
                                    } else {
                                        val igm = group.findMember(player.get())
                                        if(igm == null)
                                            p.sendMessage("This is an error and should not be here. Please contact a developer")
                                        else {
                                            group.setLeader(igm)
                                            player.get().sendMessage("You have been set as the leader of your GIM group.")
                                            p.sendMessage("${player.get().name} are now the leader of your GIM group.")
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        p.sendMessage("${player.get().name} is not a Group Ironman. Please change their gamemode first")
                    }
                } else {
                    p.sendMessage("Could not find player by name: $playerName")
                }

            }
        }

        Command(PlayerPrivilege.DEVELOPER, "forcejoingim") { p, _ ->
            p.sendInputString("Which player's group would you like to join") { playerName ->
                IronmanGroup.find(GIMTarget(username = playerName))?.join(p, true)
            }
        }

        Command(PlayerPrivilege.PLAYER, "node") { p, _ ->
            if(p.leftTheNode && p.ironmanGroupType != null || p.privilege.inherits(PlayerPrivilege.ADMINISTRATOR)) {
                if(p.area !is EdgevilleArea) {
                    p.sendMessage("You cannot use that teleport from here.")
                } else {
                    p.sendMessage("You teleport back to The Node")
                    RegularTeleport(Location(3105, 3028)).teleport(p)
                }
            } else {
                p.sendMessage("You are not allowed to go there!")
            }
        }
    }
}

class GIMTarget(override val username: String) : UsernameProvider
