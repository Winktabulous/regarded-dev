package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/04/2019 19:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArdougneSilkMerchant extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (!player.getInventory().containsItem(950, 1)) {
                    npc("I buy silk. If you ever want to sell any silk, bring it here.");
                    return;
                }
                player("Hello. I have some fine silk from Al-Kharid to sell to you.");
                npc("Ah I may be interested in that. What sort of price were you looking at per piece of silk?");
                options(TITLE, new DialogueOption("100 coins.", key(20)), new DialogueOption("400 coins.", key(80)), new DialogueOption("600 coins.", key(120)), new DialogueOption("1000 coins.", key(200)));
                player(20, "100 coins.");
                npc("Certainly.").executeAction(() -> sell(player, 100));
                player(80, "400 coins.");
                npc("You\'ll never get that much for it. I\'ll be generous and give you 150 for it.");
                options(TITLE, new DialogueOption("Ok, I guess 150 will do.", key(85)), new DialogueOption("I\'ll give it to you for 200.", key(90)), new DialogueOption("No, that is not enough.", key(95)));
                player(85, "Ok, I guess 150 will do.").executeAction(() -> sell(player, 150));
                player(90, "I\'ll give it to you for 200.");
                npc("You drive a hard bargain, but I guess that will have to do.").executeAction(() -> sell(player, 200));
                player(95, "No, that is not enough.");
                player(120, "600 coins.");
                npc("You\'ll never get that much for it. I\'ll be generous and give you 250 for it.");
                options(TITLE, new DialogueOption("Ok, I guess 250 will do.", key(125)), new DialogueOption("I\'ll give it to you for 300.", key(130)), new DialogueOption("No, that is not enough.", key(135)));
                player(125, "Ok, I guess 250 will do.").executeAction(() -> sell(player, 250));
                player(130, "I\'ll give it to you for 300.");
                npc("You drive a hard bargain, but I guess that will have to do.").executeAction(() -> sell(player, 300));
                player(135, "No, that is not enough.");
                player(200, "1000 coins.");
                npc("At that price, you can take your silk back to Al-Kharid.");
            }
        }));
    }

    private void sell(@NotNull final Player player, final int cost) {
        player.getDialogueManager().finish();
        final int count = player.getInventory().deleteItem(950, 28).getSucceededAmount();
        if (count > 0) {
            player.getInventory().addOrDrop(new Item(995, count * cost));
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SILK_MERCHANT };
    }
}
