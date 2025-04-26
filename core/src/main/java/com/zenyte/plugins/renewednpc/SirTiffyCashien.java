package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.SirTiffyCashienD;

/**
 * @author Kris | 25/11/2018 16:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SirTiffyCashien extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new SirTiffyCashienD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SIR_TIFFY_CASHIEN };
    }
}
