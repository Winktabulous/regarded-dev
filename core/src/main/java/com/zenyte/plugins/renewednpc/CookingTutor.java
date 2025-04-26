package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.lumbridge.CookingTutorD;

/**
 * @author Kris | 26/11/2018 20:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CookingTutor extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new CookingTutorD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.COOKING_TUTOR };
    }
}
