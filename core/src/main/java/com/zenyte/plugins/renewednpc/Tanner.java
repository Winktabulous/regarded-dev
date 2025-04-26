package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.alkharid.TannerD;
import com.zenyte.plugins.interfaces.TanningInterface;

/**
 * @author Kris | 25/11/2018 16:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Tanner extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new TannerD(player, npc));
        });
        bind("Trade", (player, npc) -> TanningInterface.sendTanningInterface(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ELLIS, NpcId.SBOTT, NpcId.TANNER };
    }
}
