package com.near_reality.game.packet.out.chat_channel

enum class ChatChannelType(val packetIdentifier: Int) {
    GUEST(-1),
    CLAN(0),
    GIM(1)
}
