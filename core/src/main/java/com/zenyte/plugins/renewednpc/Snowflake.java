package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Snowflake extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (!player.getInventory().containsItem(22603, 1)) {
                    npc("Good day.", Expression.WEISS_TROLL_NORMAL);
                    return;
                }
                npc("Would you like to note the basalt you have with you?", Expression.WEISS_TROLL_NORMAL);
                options("Note the basalt?", new DialogueOption("Note it.", () -> {
                    final int amount = player.getInventory().deleteItem(22603, Integer.MAX_VALUE).getSucceededAmount();
                    player.getInventory().addOrDrop(new Item(22604, amount));
                    setKey(5);
                }), new DialogueOption("Cancel."));
                npc(5, "There you go.", Expression.WEISS_TROLL_NORMAL);
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 8433, NpcId.SNOWFLAKE };
    }
}
