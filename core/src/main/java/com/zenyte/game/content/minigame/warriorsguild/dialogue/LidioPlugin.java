package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 5:34.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LidioPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Greetings warrior, how can I fill your stomach today?");
                    player("With food preferably.").executeAction(() -> player.openShop("Warrior Guild Food Shop"));
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.LIDIO };
    }
}
