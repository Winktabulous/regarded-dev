package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 18/05/2019 | 12:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BryophytaEssenceOnBattlestaffAction implements ItemOnItemAction {

    private static final Animation ANIM = new Animation(7981);
    private static final Graphics GFX = new Graphics(264, 0, 184);
    private static final Item BRYOPHYTA_STAFF = new Item(22368);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.setAnimation(ANIM);
        WorldTasksManager.schedule(() -> {
            player.setGraphics(GFX);
            player.getInventory().deleteItemsIfContains(new Item[]{ from, to }, () -> player.getInventory().addItem(BRYOPHYTA_STAFF));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 1391, 22372 };
    }
}
