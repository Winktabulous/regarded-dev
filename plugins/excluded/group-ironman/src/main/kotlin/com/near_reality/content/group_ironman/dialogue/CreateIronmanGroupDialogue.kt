package com.near_reality.content.group_ironman.dialogue

import com.near_reality.content.group_ironman.IronmanGroup
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents a [Dialogue] that opens for the [player] when selecting
 * the `Create Group` option from the group from chat channel tab interface.
 *
 * @author Stan van der Bend
 */
class CreateIronmanGroupDialogue(player: Player) : Dialogue(player) {

    override fun buildDialogue() {
        plain(
            "You are about to create a Prestige Iron Group. Prestige status " +
                    "indicates that your group has had no outside help. The status will be " +
                    "visible to players outside of your group. It will be lost upon inviting " +
                    "anyone else into your group, or grouping with outside players in " +
                    "places such as the Chambers of Xeric, the Theatre of Blood, "
        )
        plain("the Nightmare of Ashihama and the Tombs of Amascut.")
        options("Do you wish to continue making a Prestige group?") {
            "Yes - I want to show off my group's prestige." {
                IronmanGroup.form(player, prestige = true)
            }
            "No - I don't want to enable prestige." {
                IronmanGroup.form(player, prestige = false)
            }
        }
    }
}
