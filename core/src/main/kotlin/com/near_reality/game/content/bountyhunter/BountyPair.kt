package com.near_reality.game.content.bountyhunter

import com.zenyte.game.world.entity.player.Player

/**
 * This represents a pairing of a subject and their assigned target.
 * @author John J. Woloszyk / Kryeus
 */
class BountyPair(val subject: Player, val target: Player) {

    /**
     * Checks if player is a member of the pair
     */
    fun isMember(player: Player): Boolean {
        return player == subject || player == target
    }

    /**
     * Checks if player is the subject (assigned to) of the pair
     */
    fun isSubject(player: Player): Boolean {
        return player == subject
    }

    /**
     * Returns the other player in the player's pair,
     * should be called after [BountyPair.isMember]
     */
    fun getTarget(player: Player): Player {
        return if(player == subject)
            target
        else
            subject
    }

    /**
     * Checks if player is the target (assigned to kill) of the pair
     */
    fun isTarget(p: Player): Boolean {
        return p == target
    }

}