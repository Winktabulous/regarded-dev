package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

public class NeitiznotFaceGuardCreationAction implements PairedItemOnItemPlugin {
    private static final Item NEITIZNOT_FACEGUARD = new Item(24271);

    private static final Item BASILISK_JAW = new Item(24268);
    private static final Item HELM_OF_NEITIZNOT = new Item(10828);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item jaw = from.getId() == BASILISK_JAW.getId() ? from : to;
        final Item helm = from == jaw ? to : from;

        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Attach the jaw to the helmet?", new DialogueOption("Yes.", () -> player.getInventory().deleteItemsIfContains(new Item[] {helm, jaw}, () -> {
                    player.getInventory().addOrDrop(new Item(NEITIZNOT_FACEGUARD));
                    player.getDialogueManager().start(new ItemChat(player, NEITIZNOT_FACEGUARD, "You attach the Basilisk Jaw to the Helm of Neitiznot"));
                })), new DialogueOption("No."));
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(BASILISK_JAW.getId(), HELM_OF_NEITIZNOT.getId()), ItemPair.of(HELM_OF_NEITIZNOT.getId(), BASILISK_JAW.getId())};
    }
}
