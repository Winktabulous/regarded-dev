package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.OptionMessage;

/**
 * @author Kris | 25. aug 2018 : 22:39:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ReleaseableItem extends ItemPlugin {

    @Override
    public void handle() {
        bind("Release", (player, item, slotId) -> releaseOne(player, item, slotId));
    }

    public static void releaseOne(final Player player, final Item item, final int slotId) {
        if (item.getId() >= 10146 && item.getId() <= 10149) {
            player.getInventory().deleteItem(slotId, item);
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Release the " + item.getName() + (item.getAmount() > 1 ?
                                "s" :
                                "") + "?",
                        new DialogueOption("Yes, release it.", () -> player.getInventory().deleteItem(slotId, item)),
                        new DialogueOption("No, keep it."));
            }
        });

    }

    public static void releaseAll(final Player player, final Item item) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final OptionMessage options = options("Release all " + item.getName() + "s?",
                        new DialogueOption("Yes, release them.",
                                () -> player.getInventory()
                                        .deleteItem(new Item(item.getId(), player.getInventory().getAmountOf(item.getId())))),
                        new DialogueOption("No, keep them."));
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{7564, 7565, 7761, 7765, 7769, 7771, 10146, 10147, 10148, 10149, 10596, 19556, 21624};
    }

}
