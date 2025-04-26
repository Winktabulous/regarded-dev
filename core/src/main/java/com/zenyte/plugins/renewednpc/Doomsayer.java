package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.lumbridge.DoomsayerD;

/**
 * @author Kris | 26/11/2018 20:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Doomsayer extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new DoomsayerD(player, npc));
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                6773
        };
    }
}
