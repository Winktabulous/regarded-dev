package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 14/06/2019 16:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Ilfeen extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Hello, what can I help you with?");
                    options(TITLE, new DialogueOption("Could you enchant my crystal seed?", key(5)), new DialogueOption("Nothing."));
                    player(5, "Could you enchant my crystal seed?");
                    npc("What would you like me to enchant it to? It'll cost you " + (StringFormatUtil.format(Math.max(180000, 900000 - (player.getNumericAttribute("crystal recharge count").intValue() * 180000)))) + " coins and a crystal seed to do this.");
                    options(TITLE, new DialogueOption("Crystal bow", () -> enchant(player, 4212)), new DialogueOption("Crystal shield", () -> enchant(player, 4224)), new DialogueOption("Crystal halberd", () -> enchant(player, 23987)));
                }
            });
        });
    }

    private final void enchant(final Player player, final int itemId) {
        player.getDialogueManager().finish();
        final int count = player.getNumericAttribute("crystal recharge count").intValue();
        final int price = Math.max(180000, 900000 - (count * 180000));
        if (!player.getInventory().containsItem(4207, 1)) {
            player.sendMessage("You need a crystal seed to do this.");
            return;
        }
        if (!player.getInventory().containsItem(995, price)) {
            player.sendMessage("You need " + StringFormatUtil.format(price) + " coins to enchant the seed.");
            return;
        }
        player.getInventory().deleteItem(new Item(4207, 1));
        player.getInventory().deleteItem(new Item(995, price));
        player.getInventory().addItem(new Item(itemId));
        player.addAttribute("crystal recharge count", count + 1);
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(new Item(itemId), "You can imbue the " + ItemDefinitions.getOrThrow(itemId).getName().toLowerCase() + " by using an imbue scroll on it, but only while it is in its new status.");
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {8676};
    }
}
