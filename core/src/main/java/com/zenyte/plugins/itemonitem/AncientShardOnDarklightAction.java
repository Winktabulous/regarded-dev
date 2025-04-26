package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.DoubleItemChat;

/**
 * @author Tommeh | 31-1-2019 | 19:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AncientShardOnDarklightAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item shard = from.getId() == 19677 ? from : to;
        final Item darklight = from.getId() == 6746 ? from : to;
        player.getDialogueManager().start(new DoubleItemChat(player, shard, darklight, "You require additional power from the catacombs altar to create Arclight."));
    }

    @Override
    public int[] getItems() {
        return new int[] {6746, 19677};
    }
}
