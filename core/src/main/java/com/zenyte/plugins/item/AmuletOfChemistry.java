package com.zenyte.plugins.item;

import com.zenyte.game.model.item.ItemActionHandler;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AmuletOfChemistry extends ItemPlugin {
    @Override
    public void handle() {
        bind("Options", (player, item, container, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    final boolean isStopping = player.getNumericAttribute("AOC: cancel combining when out of charges").intValue() == 1;
                    options(TITLE, new DialogueOption("Check charges", () -> {
                        final int uses = player.getNumericAttribute("amulet of chemistry uses").intValue();
                        player.sendMessage("Your amulet of chemistry has " + ((5 - uses)) + " charge" + (uses == 4 ? "" : "s") + " left.");
                    }), new DialogueOption(!isStopping ? "Continue combining potions when amulet breaks" : "Stop combining potions when amulet breaks", () -> {
                        setKey(isStopping ? 5 : 10);
                        player.addAttribute("AOC: cancel combining when out of charges", isStopping ? 0 : 1);
                    }));
                    item(5, item, "You will now stop combining potions when the amulet breaks.");
                    item(10, item, "You will now continue combining potions when the amulet breaks.");
                }
            });
        });
        bind("Break", (player, item, container, slotId) -> ItemActionHandler.dropItem(player, "Destroy", slotId, 300, 500));
    }

    @Override
    public int[] getItems() {
        return new int[] {21163};
    }
}
