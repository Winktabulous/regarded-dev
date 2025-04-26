package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/11/2018 11:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SmithingSquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("Can you repair my items for me?");
                npc("Of course I'll repair it, though the materials may cost you. Just hand me the item and I'll have a look.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SQUIRE_1766 };
    }
}
