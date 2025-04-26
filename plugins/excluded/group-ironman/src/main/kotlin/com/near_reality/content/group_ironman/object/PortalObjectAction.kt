package com.near_reality.content.group_ironman.`object`

import com.near_reality.api.service.user.UserPlayerHandler
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.content.group_ironman.player.inIronmanGroupCreationInterface
import com.near_reality.content.group_ironman.player.leftTheNode
import com.near_reality.content.group_ironman.player.resetGIM
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.Dialogue.DialogueOption
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.plugins.renewednpc.ZenyteGuide

class PortalObjectAction : ObjectActionScript() {
    init {
        ObjectId.PORTAL_42819 {
            if (player.inIronmanGroupCreationInterface)
                player.dialogue { plain("You can't leave The Node while you are in the middle of creating a group!") }
            else if (player.finalisedIronmanGroup != null) {
                FadeScreen(player) {
                    player.setLocation(ZenyteGuide.SPAWN_LOCATION)
                    if (!player.leftTheNode) {
                        player.leftTheNode = true
                        ZenyteGuide.finishAppearance(player)
                    }
                }.fade(3)
            } else {
                if (!player.leftTheNode) {
                    player.dialogue {
                        plain(
                            "Since this is the first time you're leaving the island and you are not " +
                                    "in a group, you are able to change your Group Iron mode."
                        )
                        plain(
                            "If you choose a non Group Iron mode, you will be sent to " +
                                    "Lumbridge and will not be able to change mode again. Your " +
                                    "inventories will also be reset, and you will be given a new starter kit."
                        )
                        options("Would you like to change to regular ironman?") {
                            options += DialogueOption("Yes.") {
                                player.resetGIM()
                                player.leftTheNode = true
                                UserPlayerHandler.updateGameMode(player, GameMode.STANDARD_IRON_MAN) { success ->
                                    player.gameMode = GameMode.STANDARD_IRON_MAN
                                    player.setLocation(ZenyteGuide.SPAWN_LOCATION)
                                    ZenyteGuide.finishAppearance(player)
                                }
                            }
                            options += DialogueOption("No.")
                        }
                    }
                } else
                    player.sendMessage("You cannot leave the node again as a non-group ironman.")
            }
        }

    }
}
