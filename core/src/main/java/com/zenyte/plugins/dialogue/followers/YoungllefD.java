package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author William Fuhrman | 7/26/2022 11:41 AM
 * @since 05/07/2022
 */
public final class YoungllefD extends Dialogue {

    public YoungllefD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = Utils.random(2);
        switch(random) {
            case 0:
                player("I don't get it... Are you real or not?");
                npc("I'm a crystalline formation, made by the elves.");
                player("But, like... Can you feel it if I pinch you?");
                npc("Don't you even think about it.");
                break;
            case 1:
                player("What actually are you? A big wolf or something?");
                npc("I suppose I might look something like that.");
                player("That sounds like a no. What are you then?");
                npc("A hunllef.");
                player("A what?");
                npc("Nevermind.");
                player("You know, you can be a real nightmare sometimes.");
                break;
        }
    }

}
