package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/11/2018 11:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SalesmanSquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Hi, how can I help you?");
                    options(TITLE, new DialogueOption("What do you have for sale?", () -> setKey(10)), new DialogueOption("I'm fine, thanks.", () -> setKey(20)));
                    player(10, "What do you have for sale?").executeAction(() -> open(player, npc));
                    player(20, "I'm fine, thanks.");
                }
            });
        });
    }

    private void open(final Player player, final NPC npc) {
        switch(npc.getId()) {
            case 1768:
                player.openShop("Void Knight General Store");
                break;
            case 1767:
                player.openShop("Void Knight Magic Store");
                break;
            case 1765:
                player.openShop("Void Knight Archery Store");
                break;
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SQUIRE_1768, NpcId.SQUIRE_1767, NpcId.SQUIRE_1765 };
    }
}
