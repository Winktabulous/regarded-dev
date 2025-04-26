package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.FremennikIslesSailing;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.JarvaldD;

/**
 * @author Kris | 26/11/2018 18:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Jarvald extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new JarvaldD(player, npc)));
        bind("Travel", (player, npc) -> FremennikIslesSailing.sail(player, FremennikIslesSailing.SailingDestination.WATERBIRTH_ISLAND));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.JARVALD_7205, 5937 };
    }
}
