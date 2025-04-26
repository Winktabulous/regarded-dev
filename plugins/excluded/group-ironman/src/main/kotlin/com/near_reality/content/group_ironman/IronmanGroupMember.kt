package com.near_reality.content.group_ironman

import com.near_reality.game.model.ui.chat_channel.ChatChannelRank
import com.near_reality.game.world.entity.player.UsernameProvider
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import kotlinx.datetime.Instant
import java.util.Optional

/**
 * Represents a member of an [IronmanGroup].
 *
 * @author Stan van der Bend
 *
 * @param username      the [Player.getUsername] of this member.
 * @param joinTime      the [time][Instant] this member joined its [IronmanGroup].
 * @param valueReceived value of all items and gold received from other members and from shared storage.
 * @param rank          the [ChatChannelRank] of this member.
 */
data class IronmanGroupMember(
    override val username: String,
    val joinTime: Instant,
    var valueReceived: Long,
    var rank: ChatChannelRank = ChatChannelRank.CLAN_RANK_1,
) : UsernameProvider {

    /**
     * The [IronmanGroup] this member is a part of, if any, or else `null`.
     */
    val group: IronmanGroup?
        get() = IronmanGroup.find(this)

    /**
     * Gets the [trading restriction][IronmanGroupRestriction.Trading] applied to this member.
     */
    val tradeRestriction: IronmanGroupRestriction.Trading
        get() = IronmanGroupRestriction.Trading.find(this)

    /**
     * Executes a [block] if the [Player] associated with the [username] is online.
     */
    fun ifOnline(block: Player.() -> Unit) =
        World.getPlayerByUsername(username)?.run(block)

    fun findPlayer() =
        Optional.ofNullable(World.getPlayerByUsername(username))

    fun isOnline() =
        World.getPlayerByUsername(username) != null
}
