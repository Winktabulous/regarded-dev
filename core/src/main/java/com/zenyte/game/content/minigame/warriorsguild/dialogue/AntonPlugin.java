package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 6:55.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AntonPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Ahhh, hello there. How can I help?");
                    player("Looks like you have a good selection of weapons around here...");
                    npc("Indeed so, specially imported from the finest smiths " + "around the lands, take a look at my wares.").executeAction(() -> player.openShop("Warrior Guild Armoury"));
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ANTON };
    }
}
