package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.minigame.nmz.DominicOnionD;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 26/11/2018 18:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DominicOnion extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new DominicOnionD(player, "Talk-to"));
        });
        bind("Dream", (player, npc) -> player.getDialogueManager().start(new DominicOnionD(player, "Dream")));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.DOMINIC_ONION };
    }
}
