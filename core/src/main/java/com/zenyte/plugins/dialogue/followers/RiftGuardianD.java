package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 26-3-2019 | 21:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RiftGuardianD extends Dialogue {


    public RiftGuardianD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        switch (Utils.random(2)) {
            case 0:
                player("Can you see your own rift?");
                npc("No. From time to time I feel it shift and change inside me though. It is an odd feeling.");
                break;
            case 1:
                player("Where would you like me to take you today Rifty?");
                npc("Please do not call me that... we are a species of honour " + player.getName() + ".");
                player("Sorry.");
                break;
            case 2:
                player("Hey! What's that!");
                npc("Huh, what?! Where?");
                player("Not the best guardian it seems.");
                break;
        }
    }
}
