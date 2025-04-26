package com.near_reality.game.packet.out.chat_channel

interface ChatChannelMemberIdentifier {
    val hash: Long
    val name: String
}
