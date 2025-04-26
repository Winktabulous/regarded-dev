package com.zenyte.game.model.ui.testinterfaces;

import com.near_reality.game.model.ui.chat_channel.ChatChannelPlayerExtKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.spells.teleports.MinigameGroupFinder;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.enums.EnumDefinitions;

/**
 * @author Kris | 25/10/2018 17:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * The "Join" button is handled automatically by CS2s.
 */
public class MinigameTabInterface extends Interface {
    public static final int MINIGAME_LIST_ENUM = 848;

    @Override
    protected void attach() {
        put(26, "Teleport");
        put(18, "Select minigame");
    }

    @Override
    public void open(Player player) {
        if (player.getRaid().isPresent()) {
            GameInterface.RAID_PARTY_TAB.open(player);
            return;
        }
        ChatChannelPlayerExtKt.getChatChannelInterfaceType(player).sendTabInterface(player, getInterface());
        final int lastMinigameSlot = EnumDefinitions.get(MINIGAME_LIST_ENUM).getSize() + 1;
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Teleport"), 1, lastMinigameSlot, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Select minigame"), 1, lastMinigameSlot, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Teleport", (player, slotId, itemId, option) -> {
            final MinigameGroupFinder minigame = MinigameGroupFinder.getMinigame(slotId);
            if (minigame == null) {
                player.sendMessage("That minigame's teleport is not supported yet.");
                return;
            }
            minigame.teleport(player);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GROUPING_TAB;
    }
}
