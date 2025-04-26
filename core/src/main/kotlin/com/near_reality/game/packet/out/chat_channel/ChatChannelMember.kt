package com.near_reality.game.packet.out.chat_channel

import com.near_reality.game.model.ui.chat_channel.ChatChannelRank

/**
 * @param extraInfo something to do with script `ACTIVECLANSETTINGS_GETAFFINEDEXTRAINFO = 3814`
 */
class ChatChannelMember(
    override val hash: Long,
    override val name: String,
    val rank: ChatChannelRank,
    val extraInfo: Int,
    val field1649: Int,
    val field1650: Boolean,
    val world: Int = 1,
) : ChatChannelMemberIdentifier
