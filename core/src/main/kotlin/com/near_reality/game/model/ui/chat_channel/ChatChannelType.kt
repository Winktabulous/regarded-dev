package com.near_reality.game.model.ui.chat_channel

import com.zenyte.game.GameInterface

enum class ChatChannelType(val gameInterface: GameInterface) {
    ChatChannel(GameInterface.CHAT_CHANNEL_TAB),
    YourClan(GameInterface.YOUR_CLAN_TAB),
    ViewAnotherClan(GameInterface.ANOTHER_CLAN_TAB),
    Grouping(GameInterface.GROUPING_TAB),
    IronGroup(GameInterface.DEFAULT_GIM_TAB),
    HardcoreIronGroup(GameInterface.DEFAULT_GIM_TAB),
}
