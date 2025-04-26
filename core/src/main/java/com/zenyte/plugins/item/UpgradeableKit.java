package com.zenyte.plugins.item;

import com.zenyte.game.model.item.enums.UpgradeKit;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 07/05/2019 20:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UpgradeableKit extends ItemPlugin {
    @Override
    public void handle() {
        bind("Revert", (player, item, slotId) -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to revert this item.");
                return;
            }
            final UpgradeKit dis = UpgradeKit.MAPPED_VALUES.get(item.getId());
            if (dis == null) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Revert this item? You will not get the upgrade kit back.", new DialogueOption("Yes, revert it.", () -> {
                        if (player.getInventory().getItem(slotId) != item) {
                            return;
                        }
                        player.getInventory().deleteItem(slotId, item);
                        player.getInventory().addItem(dis.getBaseItem(), 1);
                        player.sendMessage("You revert the " + item.getName() + " to its original form.");
                    }), new DialogueOption("No, don't revert it."));
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final UpgradeKit val : UpgradeKit.values) {
            list.add(val.getCompleteItem());
        }
        return list.toArray(new int[0]);
    }
}
