package com.zenyte.plugins.itemonplayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnPlayerPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.teleportsystem.TeleportScroll;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Corey
 * @since 09/09/2019
 */
public class ItemOnOrFromIronmanPlugin implements ItemOnPlayerPlugin {
    @Override
    public void handleItemOnPlayerAction(final Player giver, final Item item, final int slot, final Player target) {
        if (!giver.isIronman() && !target.isIronman()) {
            giver.sendMessage("Nothing interesting happens.");
            return;
        }
        giver.getDialogueManager().start(new Dialogue(giver) {
            @Override
            public void buildDialogue() {
                item(item, "Give " + target.getName() + " the " + Colour.BLUE.wrap(item.getName()) + "?");
                options(TITLE, new DialogueOption("Give them the item.", () -> {
                    if (isUnavailable(giver) || isUnavailable(target)) {
                        player.sendMessage("Unable to process request.");
                        return;
                    }
                    if (player.getInventory().getItem(slot) != item) {
                        return;
                    }
                    giver.sendMessage(target.getName() + " can now take the item from the ground.");
                    target.sendMessage(Colour.RED.wrap(giver.getName() + " has dropped something for you to take."));
                    giver.getInventory().set(slot, null);
                    World.spawnFloorItem(item, target, 3000, 0);
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public int[] getItems() {
        final IntOpenHashSet set = new IntOpenHashSet();
        set.addAll(TeleportScroll.map.keySet());
        set.add(30031);
        return set.toIntArray();
    }
}
