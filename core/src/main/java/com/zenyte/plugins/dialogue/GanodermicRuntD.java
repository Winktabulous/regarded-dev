package com.zenyte.plugins.dialogue;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author William Fuhrman | 8/7/2022 1:27 PM
 * @since 05/07/2022
 */
public final class GanodermicRuntD extends Dialogue {

    public GanodermicRuntD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = Utils.random(2);
        switch(random) {
            case 0:
                npc("Grrroooooaarrr...");
                player("You don't seem so scary to me.");
                break;
            case 1:
                player("Hi there, little fella!");
                player("Who's a cute little behemoth? You are!");
                npc("Grrr! Grrrr, grrrr!");
                player("Sooo cute!");
                break;
        }
    }

}