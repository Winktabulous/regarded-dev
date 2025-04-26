package com.near_reality.content.group_ironman.dialogue

import com.near_reality.api.service.user.UserPlayerHandler
import com.near_reality.content.group_ironman.IronmanGroupType
import com.near_reality.content.group_ironman.player.ironmanGroupType
import com.near_reality.game.model.ui.chat_channel.selectedChatChannelType
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents a [Dialogue] for changing the [player]'s [group iron man mode][IronmanGroupType].
 *
 * This dialogue can be opened by using one of the armour crates at The Node.
 */
class ChangeGroupIronManModeDialogue(
    player: Player,
    private val newGroupType: IronmanGroupType
) : Dialogue(player) {

    override fun buildDialogue() {
        options("Would you like to change to ${newGroupType.formattedName} mode?") {
            "Yes." {
                player.animation = Animation.GRAB
                val previousGroupType = player.ironmanGroupType
                UserPlayerHandler.updateGameMode(player, newGroupType.gameMode) { success ->
                    if (success) {
                        player.selectedChatChannelType = newGroupType.channelType
                        if (previousGroupType != null) {
                            player.equipment.deleteItem(previousGroupType.helmetId, 1)
                            player.inventory.deleteItem(previousGroupType.helmetId, 1)
                        }
                        player.inventory.addItem(newGroupType.helmetId, 1)
                    }
                }
            }
            "No." {}
        }
    }
}
