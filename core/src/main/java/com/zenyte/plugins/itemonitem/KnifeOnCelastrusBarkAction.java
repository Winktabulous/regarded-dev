package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.skills.CelastrusBarkFletchingD;

/**
 * @author Tommeh | 19/11/2019 | 21:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KnifeOnCelastrusBarkAction implements ItemOnItemAction {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getDialogueManager().start(new CelastrusBarkFletchingD(player));
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.CELASTRUS_BARK, ItemId.KNIFE };
    }
}
