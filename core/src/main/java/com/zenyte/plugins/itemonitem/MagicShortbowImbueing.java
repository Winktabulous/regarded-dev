package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 08/06/2019 09:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicShortbowImbueing implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(from, to, "Imbue the shortbow with the scroll? You cannot get the scroll back afterwards.");
                options("Imbue the shortbow?", new DialogueOption("Imbue it.", () -> {
                    if (player.getInventory().getItem(fromSlot) != from || player.getInventory().getItem(toSlot) != to) {
                        return;
                    }
                    player.getInventory().deleteItem(from);
                    player.getInventory().deleteItem(to);
                    player.getDialogueManager().finish();
                    player.getInventory().addItem(new Item(12788));
                    player.getDialogueManager().start(new ItemChat(player, new Item(12788), "You imbue your shortbow with the scroll."));
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(12786, 861)
        };
    }
}
