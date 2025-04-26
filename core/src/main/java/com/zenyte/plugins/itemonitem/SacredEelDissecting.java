package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.DoubleItemChat;

/**
 * @author Kris | 14/03/2019 19:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SacredEelDissecting implements PairedItemOnItemPlugin {
    private static final Animation animation = new Animation(5244);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item knife = from.getId() == 946 ? from : to;
        final Item eel = knife == from ? to : from;
        player.getActionManager().setAction(new Action() {
            private int level;
            @Override
            public boolean start() {
                if ((level = player.getSkills().getLevel(SkillConstants.COOKING)) < 72) {
                    player.getDialogueManager().start(new DoubleItemChat(player, knife, eel, "You need a Cooking level of at least 72 to dissect the sacred eel."));
                    return false;
                }
                if (!player.getInventory().containsItem(eel)) {
                    return false;
                }
                player.setAnimation(animation);
                delay(1);
                return true;
            }
            @Override
            public boolean process() {
                return true;
            }
            @Override
            public int processWithDelay() {
                final int scales = Math.min(7, (3 + (level - 72 / 7))) + Utils.random(2);
                player.getSkills().addXp(SkillConstants.COOKING, 100 + (scales * 3));
                final Inventory inventory = player.getInventory();
                inventory.deleteItem(eel);
                inventory.addItem(new Item(12934, scales));
                SherlockTask.DISSECT_SACRED_EEL.progress(player);
                if (!inventory.containsItem(eel)) {
                    return -1;
                }
                player.setAnimation(animation);
                return 1;
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(946, 13339)};
    }
}
