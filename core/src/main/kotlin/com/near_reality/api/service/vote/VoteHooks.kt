package com.near_reality.api.service.vote

import com.google.common.eventbus.Subscribe
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.plugins.events.LoginEvent


/**
 * Represents a collection of hooks that are related to voting.
 *
 * @author Stan van der Bend
 */
object VoteHooks {

    /**
     * Registers a login event hook that will remind the player to vote if they haven't done so today.
     */
    @Subscribe
    @JvmStatic
    fun onLogin(event: LoginEvent) {
        val player = event.player
        if (player.getBooleanAttribute("registered")
            && System.currentTimeMillis() >= player.lastVoteClaimTime) {
            player.dialogue {
                item(Item(32148), "${Colour.RS_RED.wrap("Vote For A Reward!")}<br>" +
                        "It seems you have not voted today, vote now for ${Colour.DARK_BLUE.wrap("Vote Points")}, " +
                        "along with a chance of receiving ${Colour.DARK_BLUE.wrap("50")} Store Credits " +
                        "and an extra ${Colour.DARK_BLUE.wrap("50%")} blood money bonus per kill " +
                        "(each claimed vote lasts 15 minutes)")
            }
        }
    }
}
