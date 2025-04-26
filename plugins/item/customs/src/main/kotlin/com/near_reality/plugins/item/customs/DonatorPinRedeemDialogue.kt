package com.near_reality.plugins.item.customs

import com.near_reality.api.model.Bond
import com.near_reality.api.service.user.UserPlayerHandler
import com.near_reality.game.content.commands.DeveloperCommands
import com.zenyte.game.GameConstants
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles the redeeming of donator pin items, works through the API,
 * so is disabled for world instances where the API is not active.
 *
 * @author Stan van der Bend
 */
class DonatorPinRedeemDialogue(player: Player, private val item: Item) : Dialogue(player) {

    override fun buildDialogue() {

        if (!DeveloperCommands.enabledDPinRedeeming){
            if (GameConstants.isOwner(player)) {
                player.sendDeveloperMessage(
                    "You are allowed to exchange donator pins even though they are disabled for others " +
                            "because you're just simply cooler than the rest.")
            } else {
                player.dialogue {
                plain("Redeeming donator pins is currently disabled.")
            }
                return
            }
        }

        if (GameConstants.WORLD_PROFILE.private || GameConstants.WORLD_PROFILE.isBeta()) {
            if (GameConstants.isOwner(player)) {
                player.sendDeveloperMessage(
                    "You are allowed to exchange donator pins on this world " +
                        "because you're just simply cooler than the rest.")
            } else {
                player.dialogue { plain("You cannot exchange donator pins on this world.") }
                return
            }
        }

        val bond = Bond[item.id] ?: error("Invalid dialogue initialised for non donator-pin item (item=$item)")
        val shade = bond.getShade()
        val shadedName = "$shade${item.name}</shad>"
        item(item, "Would you like to redeem this $shadedName " +
                "for ${Colour.RS_GREEN.wrap("${bond.credits} store credits")} " +
                "and ${Colour.DARK_BLUE.wrap("$${bond.amount}")} added to your total spent?")

        options("Redeem the $shadedName?") {
            "Yes." {
                UserPlayerHandler.claimBond(player, bond, shadedName)
            }
            "No." {}
        }
    }
}

fun Bond.getShade() = when (this) {
    Bond.DONATOR_PIN_10 -> "<shad=00FF00>"
    Bond.DONATOR_PIN_25 -> "<shad=FC02E7>"
    Bond.DONATOR_PIN_50 -> "<shad=006d62>"
    Bond.DONATOR_PIN_100 -> "<shad=600000>"
}
