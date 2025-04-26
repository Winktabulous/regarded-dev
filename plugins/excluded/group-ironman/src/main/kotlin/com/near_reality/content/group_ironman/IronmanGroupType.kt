package com.near_reality.content.group_ironman

import com.near_reality.game.model.ui.chat_channel.ChatChannelType
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.privilege.GameMode

enum class IronmanGroupType(
    val formattedName: String,
    val gameMode: GameMode,
    val channelType: ChatChannelType,
    val helmetId: Int,
) {
    NORMAL(
        "Group Iron",
        GameMode.GROUP_IRON_MAN,
        ChatChannelType.IronGroup,
        ItemId.GROUP_IRON_HELM,
    ),
    HARDCORE(
        "Hardcore Group Iron",
        GameMode.GROUP_HARDCORE_IRON_MAN,
        ChatChannelType.HardcoreIronGroup,
        ItemId.HARDCORE_GROUP_IRON_HELM
    );

    companion object {
        val values = values()
    }

}
