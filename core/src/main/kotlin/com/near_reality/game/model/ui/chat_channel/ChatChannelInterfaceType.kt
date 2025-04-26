package com.near_reality.game.model.ui.chat_channel

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.`var`.VarCollection

enum class ChatChannelInterfaceType(
    val gameInterface: GameInterface,
    val paneType: PaneType,
    val paneComponentId: Int,
    val varCollection: VarCollection,
    vararg val components: ChatChannelType,
) {
    Regular(
        GameInterface.REGULAR_CHAT_CHANNELS,
        PaneType.CHAT_TAB_HEADER,
        7,
        VarCollection.ACTIVE_SOCIAL_TAB,
        ChatChannelType.ChatChannel,
        ChatChannelType.YourClan,
        ChatChannelType.ViewAnotherClan,
        ChatChannelType.Grouping
    ),
    GroupIronMan(
        GameInterface.GIM_CHAT_CHANNELS,
        PaneType.IRON_GROUP_SOCIALS_TAB_HEADER,
        8,
        VarCollection.ACTIVE_SOCIAL_TAB,
        ChatChannelType.IronGroup,
        ChatChannelType.HardcoreIronGroup,
        ChatChannelType.ChatChannel,
        ChatChannelType.YourClan,
        ChatChannelType.ViewAnotherClan,
        ChatChannelType.Grouping
    );

    fun sendTabInterface(player: Player, tabInterface: GameInterface) {
        player.interfaceHandler.sendInterface(
            tabInterface.id,
            paneComponentId,
            paneType,
            true
        )
    }
}
