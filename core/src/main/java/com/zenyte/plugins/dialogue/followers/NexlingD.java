package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;


/**
 * @author William Fuhrman | 7/26/2022 10:40 AM
 * @since 05/07/2022
 */
public final class NexlingD extends Dialogue {

    public NexlingD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = Utils.random(2);
        switch (random) {
            case 0:
                npc("Flood my lungs with blood!");
                player("Now that just sounds plain unhealthy.");
                npc("You're not meant to take it literally.");
                player("Oh, fair enough.");
                npc("Fill my soul with smoke!");
                return;
            case 1:
                npc("Fumus! Umbra! Cruor! Glacies! Don't fail me!");
                player("Err...");
                npc("Fumus? Where is Fumus?");
                player("You know they're not here, right?");
                npc("But... Cruor?");
                return;
        }
    }
}
