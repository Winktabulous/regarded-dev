package com.zenyte.plugins.item.trident;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Christopher
 * @since 2/20/2020
 */
public class TridentOfTheSwamp extends ItemPlugin {
    @Override
    public void handle() {
        bind("Dismantle", (player, item, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Dismantle the trident.", () -> {
                        if (player.getInventory().getItem(slotId) == item) {
                            dismantle(player, item, slotId);
                        }
                    }), new DialogueOption("Keep the trident intact."));
                }
            });
        });
        bind("Uncharge", (player, item, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Uncharge the trident.", () -> {
                        if (player.getDuel() != null && player.getDuel().inDuel()) {
                            player.sendMessage("You can't do this during a duel.");
                            return;
                        }
                        if (player.getInventory().getItem(slotId) == item) {
                            uncharge(player, item, slotId);
                        }
                    }), new DialogueOption("Keep the charges in the trident.."));
                }
            });
        });
    }

    private void dismantle(final Player player, final Item item, final int slotId) {
        final SwampTrident tridentDef = SwampTrident.unchargedMap.get(item.getId());
        final Inventory inventory = player.getInventory();
        if (!inventory.hasFreeSlots()) {
            player.sendMessage("You need some more free space to dismantle the trident.");
            return;
        }
        inventory.replaceItem(tridentDef.getBaseId(), 1, slotId);
        inventory.addOrDrop(new Item(ItemId.MAGIC_FANG));
        player.sendMessage("You dismantle the magic fang from the trident.");
    }

    private void uncharge(final Player player, final Item item, final int slotId) {
        final SwampTrident tridentDef = SwampTrident.chargedMap.get(item.getId());
        tridentDef.uncharge(player, item, slotId);
    }

    @Override
    public int[] getItems() {
        return SwampTrident.allTridentsMap.keySet().toIntArray();
    }
}
