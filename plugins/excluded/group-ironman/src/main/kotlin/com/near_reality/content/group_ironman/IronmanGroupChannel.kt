package com.near_reality.content.group_ironman

import com.near_reality.content.group_ironman.player.sendChatChannelPacket
import com.near_reality.content.group_ironman.player.sendChatChannelSettingsPacket
import com.near_reality.game.model.ui.chat_channel.ChatChannelRank
import com.near_reality.game.packet.out.chat_channel.*
import com.near_reality.game.util.HuffmanManager
import com.zenyte.game.packet.out.MessageClanChannel
import com.zenyte.game.world.entity.masks.ChatMessage
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.utils.TextUtils

/**
 * Handles the chat channel mechanics for the argued [group].
 *
 * @author Leanbow, Stan van der Bend
 */
class IronmanGroupChannel(private val group: IronmanGroup) {

    /**
     * Represents the currently online group [IronmanGroup.activeMembers].
     */
    private val membersInChannel: MutableList<Player> by lazy { mutableListOf() }

    /**
     * Adds the [player] to [membersInChannel].
     */
    fun add(player: Player, update: Boolean = true) {
        player.delay(1) {
            player.sendMessage(IronmanGroup.JOIN_GIM_CHANNEL_MESSAGE, MessageType.CLAN)
        }
        membersInChannel.add(player)
        if (update) {
            refreshChannel()
        }
    }

    /**
     * Removes the [player] from [membersInChannel].
     */
    fun remove(player: Player) {
        membersInChannel.remove(player)
        refreshChannel()
    }

    /**
     * Set the argued [chatChannelVar] to [value] in [variables] and
     * updates the chat channel settins for the online members.
     */
    fun setVariableInt(chatChannelVar: ChatChannelVar, value: Int) {
        group.variables[chatChannelVar] = value
        membersInChannel.forEach { player ->
            player.sendChatChannelSettingsPacket(group)
        }
    }

    fun refreshChannel() {
        membersInChannel.forEach { player ->
            player.sendChatChannelPacket(group)
        }
    }

    /**
     * Sends the [message] to all [membersInChannel].
     */
    fun sendPlayerMessage(message: String, name: String) {
        membersInChannel.forEach { member ->
            member.send(MessageClanChannel(
                ChatChannelType.GIM,
                TextUtils.formatName(name),
                ChatMessage(HuffmanManager.encodeHuffmanBuf(message), message, 0, false)))
        }
    }

    fun sendMessage(message: String) {
        membersInChannel.forEach { member ->
            member.sendMessage("|$message", MessageType.CLAN)
        }
    }

    /**
     * Creates a [ChatChannelSettings] packet based on the state of this [IronmanGroupChannel].
     */
    fun toSettingsPacket(): ChatChannelSettings {
        val packetMembers = mutableListOf<ChatChannelMember>()
        group.activeMembers.forEach {
            packetMembers.add(
                ChatChannelMember(
                    hash = 0,
                    name = TextUtils.formatName(it.username),
                    rank = ChatChannelRank.CLAN_RANK_1,
                    extraInfo = 0,
                    field1649 = 0,
                    field1650 = false
                )
            )
        }
        return ChatChannelSettings.Add(
            type = ChatChannelType.GIM,
            version = 3,
            useNames = true,
            useHashes = false,
            field1636 = 0,
            field1638 = 0,
            members = packetMembers,
            bannedMembers = emptyList(),
            name = group.name,
            permissions = ChatChannelSettingsDelta.Change.Permissions(
                allowGuests = false,
                talkRank = 0,
                kickRank = 0,
                lootShareRank = 0,
                coinShareRank = 0
            ),
            variables = group.variables
        )
    }

    /**
     * Creates a [ChatChannelUpdate] packet based on the state of this [IronmanGroupChannel].
     */
    fun toUpdatePacket(): ChatChannelUpdate {
        val packetMembers = mutableListOf<ChatChannelMember>()
        membersInChannel.forEach {
            packetMembers.add(ChatChannelMember(
                hash = 0,
                name = it.username,
                rank = ChatChannelRank.CLAN_RANK_1,
                extraInfo = 0,
                field1649 = 0,
                field1650 = false
            ))
        }
        return ChatChannelUpdate.Add(
            type = ChatChannelType.GIM, version = 2,
            useNames = true,
            useHashes = false,
            key = 0,
            field1709 = 0,
            members = packetMembers,
            name = group.name,
            permissions = ChatChannelSettingsDelta.Change.Permissions(
                allowGuests = false,
                talkRank = 0,
                kickRank = 0,
                lootShareRank = 0,
                coinShareRank = 0
            ),
        )
    }
}
