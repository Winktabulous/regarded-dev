package com.zenyte.plugins.item.trident;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Christopher
 * @since 2/20/2020
 */
public class TridentOfTheSeas extends ItemPlugin {
    @Override
    public void handle() {
        bind("Uncharge", (player, item, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Uncharge the trident. The coins won't be refunded.", () -> {
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

    private void uncharge(final Player player, final Item item, final int slotId) {
        final SeaTrident tridentDef = SeaTrident.chargedMap.get(item.getId());
        tridentDef.uncharge(player, item, slotId);
    }

    @Override
    public int[] getItems() {
        return SeaTrident.chargedMap.keySet().toIntArray();
    }
}
