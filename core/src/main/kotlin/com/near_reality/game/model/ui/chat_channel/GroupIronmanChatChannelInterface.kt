package com.near_reality.game.model.ui.chat_channel

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface

/**
 * Handles the group ironman variant of the social tabs interface.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class GroupIronmanChatChannelInterface : Interface() {

    override fun attach() {
        put(3, "Iron Group")
        put(4, "Clat-channel")
        put(5, "Your Clan")
        put(6, "View another clan")
        put(7, "Grouping")
    }

    override fun build() {
        bind("Iron Group", ChatChannelType.IronGroup)
        bind("Clat-channel", ChatChannelType.ChatChannel)
        bind("Your Clan", ChatChannelType.YourClan)
        bind("View another clan", ChatChannelType.ViewAnotherClan)
        bind("Grouping", ChatChannelType.Grouping)
    }

    private fun bind(componentName: String, tabType: ChatChannelType) =
        bind(componentName) { player -> player.selectedChatChannelType = tabType }

    override fun getInterface() =
        GameInterface.GIM_CHAT_CHANNELS
}
