package com.near_reality.game.model.ui.chat_channel

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface

/**
 * Formerly named `ChatHeaderTabInterface`
 *
 * @author Tommeh
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class RegularChatChannelInterface : Interface() {

    override fun attach() {
        put(3, "Chat-channel")
        put(4, "Your Clan")
        put(5, "View another clan")
        put(6, "Grouping")
    }

    override fun build() {
        bind("Chat-channel", ChatChannelType.ChatChannel)
        bind("Your Clan", ChatChannelType.YourClan)
        bind("View another clan", ChatChannelType.ViewAnotherClan)
        bind("Grouping", ChatChannelType.Grouping)
    }

    private fun bind(componentName: String, tabType: ChatChannelType) =
        bind(componentName) { player -> player.selectedChatChannelType = tabType }

    override fun getInterface() =
        GameInterface.REGULAR_CHAT_CHANNELS
}
