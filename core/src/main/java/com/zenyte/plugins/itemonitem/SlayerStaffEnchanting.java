package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 10/05/2019 23:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SlayerStaffEnchanting implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item staff = from.getId() == 4170 ? from : to;
        final Item enchantment = staff == from ? to : from;
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(new Item(21255), "Enchanting the staff consumes one enchantment scroll & turns the staff untradable. Are you sure you wish to continue?");
                options("Enchant the staff?", new DialogueOption("Yes.", () -> {
                    if (player.getInventory().getItem(fromSlot) != from || player.getInventory().getItem(toSlot) != to) {
                        return;
                    }
                    player.getInventory().deleteItem(new Item(enchantment.getId(), 1));
                    player.getInventory().set(staff == from ? fromSlot : toSlot, new Item(21255, 1, 2500));
                    player.getDialogueManager().finish();
                    player.getDialogueManager().start(new ItemChat(player, new Item(21255), "You enchant your " +
                            "slayer's staff with the enchantment scroll."));
                }), new DialogueOption("No."));
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(4170, 21257)};
    }
}
