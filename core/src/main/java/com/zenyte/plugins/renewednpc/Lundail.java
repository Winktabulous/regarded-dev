package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.magebank.LundailD;

/**
 * @author Kris | 25/11/2018 20:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Lundail extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new LundailD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
            1601
        };
    }
}
